package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.spring.config.StoreVariablesConfig;
import de.fraunhofer.isst.health.store.utils.RepositoryManagement;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.ServiceTask;
import dev.dsf.bpe.v2.variables.Variables;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateKubernetesChart implements ServiceTask
{
	private static final Logger logger = LoggerFactory.getLogger(CreateKubernetesChart.class);

	StoreVariablesConfig storeVariablesConfig;

	public CreateKubernetesChart(StoreVariablesConfig storeVariablesConfig)
	{
		this.storeVariablesConfig = storeVariablesConfig;
	}

	@Override
	public void execute(ProcessPluginApi api, Variables variables)
	{
		Task task = variables.getStartTask();

		String projectIdentifier = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER);

		String bussinessKey = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY);

		logger.info(
				"Create store in kubernetes engine of approved data sharing project [project-identifier: {}; task-id: {}]",
				projectIdentifier,
				task.getId());

		String storeId = (bussinessKey + projectIdentifier)
				.toLowerCase()
				.replace("-", "");

        storeId = DigestUtils.md5Hex(storeId);

		try
		{
			createFhirStore(storeId);
			variables.setString(StoreControllerConstants.FHIRSTOREURL, storeVariablesConfig.getStoreHostname()+"-"+ storeId + "/fhir");
		}
		catch (GitAPIException | IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	private void createFhirStore(String storeId) throws GitAPIException, IOException
	{
		logger.info("Checking out repository");
		RepositoryManagement repositoryManagement = new RepositoryManagement(
				storeId,
				storeVariablesConfig.getGitUrl(),
				storeVariablesConfig.getGitBranch(),
				storeVariablesConfig.getGitUsername(),
				storeVariablesConfig.getGitCredentials());

		repositoryManagement.cloneInto("Repository-" + storeId);

		File values = repositoryManagement.readFile("development/dmu/","values.yaml");
		Map<String, Object> data = loadYaml(values);

		// Access "stores"
		List<String> stores = (List<String>) data.get("stores");
		if (stores == null) {
			stores = new ArrayList<>();
		}

		if (!stores.contains(storeId)) {
			stores.add(storeId);
		}

		data.put("stores", stores);
		writeYaml(data, values);

		repositoryManagement.addAll();
		repositoryManagement.commit("Added File Storage to archive project with id " + storeId);
		repositoryManagement.push();
		repositoryManagement.close();

	}


	public Map<String, Object> loadYaml(File file)
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

	public void writeYaml(Map<String, Object> data, File file){
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



}
