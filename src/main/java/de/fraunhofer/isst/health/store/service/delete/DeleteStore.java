package de.fraunhofer.isst.health.store.service.delete;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.spring.config.StoreVariablesConfig;
import de.fraunhofer.isst.health.store.utils.RepositoryManagement;
import de.medizininformatik_initiative.processes.common.util.ConstantsBase;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.ServiceTask;
import dev.dsf.bpe.v2.constants.CodeSystems;
import dev.dsf.bpe.v2.variables.Variables;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteStore implements ServiceTask {
    private static final Logger logger = LoggerFactory.getLogger(DeleteStore.class);
    StoreVariablesConfig storeVariablesConfig;

    public DeleteStore(StoreVariablesConfig storeVariablesConfig) {
        this.storeVariablesConfig = storeVariablesConfig;
    }

    @Override
    public void execute(ProcessPluginApi api, Variables variables) {
        Task task = variables.getStartTask();

        String projectIdentifier = getProjectIdentifier(task);
        variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER, projectIdentifier);

        String businessKey = getBussinessKey(task, api);
        variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY, businessKey);

        logger.info(
                "Delete store in kubernetes of data sharing project [project-identifier: {}; task-id: {}]",
                projectIdentifier,
                task.getId()
        );

        String storeId = (businessKey + projectIdentifier)
                .toLowerCase()
                .replace("-", "");

        storeId = DigestUtils.md5Hex(storeId);

        try {
            deleteFhirStore(storeId);
            variables.setString(StoreControllerConstants.FHIRSTOREURL, storeVariablesConfig.getStoreHostname()+"-"+ storeId + "/fhir");
        } catch (GitAPIException | IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private void deleteFhirStore(String storeId) throws GitAPIException, IOException {
        logger.info("Checking out repository");
        RepositoryManagement repositoryManagement = new RepositoryManagement(
                storeId,
                storeVariablesConfig.getGitUrl(),
                storeVariablesConfig.getGitBranch(),
                storeVariablesConfig.getGitUsername(),
                storeVariablesConfig.getGitCredentials());

        repositoryManagement.cloneInto("Repository-" + storeId);

        logger.info("Modifying repository");
        //Delete entry in values.yaml
        File values = repositoryManagement.readFile("development/dmu/","values.yaml");
        Map<String, Object> data = loadYaml(values);

        List<String> stores = (List<String>) data.get("stores");
        if (stores == null) {
            stores = new ArrayList<>();
        }

        List<String> storeResults = stores.stream().filter(e -> e.equals(storeId)).toList();
        if (storeResults.size() != 1) {
            throw new RuntimeException("Could not find exactly one store in yaml. StoreCount: " + storeResults.size());
        }

        stores.remove(storeResults.get(0));
        writeYaml(data, values);

        //Delete entries in dmu-stores-db.yaml
        File db = repositoryManagement.readFile("development/dmu/templates/databases/","dmu-stores-db.yaml");
        data = loadYaml(db);

        Map<String, Object> spec = (Map<String, Object>) data.get("spec");
        Map<String, String> databases = (Map<String, String>) spec.get("databases");

        String dbId = "db"+storeId;
        databases.remove(dbId);
        writeYaml(data, db);

        logger.info("Commiting changes");
        repositoryManagement.addAll();
        repositoryManagement.commit("Removed FHIR-Store with id " + storeId);
        logger.info("Pushing changes");
        repositoryManagement.push();
        repositoryManagement.close();
        logger.info("Store \"" + storeId + "\" deleted");
    }

    private Map<String, Object> loadYaml(File file)
    {
        // Load YAML
        Yaml yaml = new Yaml();
        Map<String, Object> data;
        try (InputStream input = new FileInputStream(file)) {
            data = yaml.load(input);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return data;
    }

    private void writeYaml(Map<String, Object> data, File file){
        // Write back to file
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);

        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private String getProjectIdentifier(Task task)
    {
        return task.getInput().stream().filter(i -> i.getType().getCoding().stream()
                        .anyMatch(c -> StoreControllerConstants.CODESYSTEM_DATA_SHARING.equals(c.getSystem())
                                && StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER.equals(c.getCode())))
                .filter(i -> i.getValue() instanceof Identifier).map(i -> (Identifier) i.getValue())
                .filter(i -> ConstantsBase.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER.equals(i.getSystem()))
                .map(Identifier::getValue).map(String::trim).findFirst().orElseThrow(() -> new RuntimeException(
                        "No project-identifier present in task with id '" + task.getId() + "'"));
    }

    private String getBussinessKey(Task task, ProcessPluginApi api)
    {
        return api.getTaskHelper()
                .getFirstInputParameterValue(task, CodeSystems.BpmnMessage.businessKey(), StringType.class)
                .orElseThrow(() -> new RuntimeException("Business Key is missing")).getValue();
    }
}
