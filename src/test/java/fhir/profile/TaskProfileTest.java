package fhir.profile;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.StoreControllerProcessPluginDefinition;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Task.TaskIntent;
import org.hl7.fhir.r4.model.Task.TaskStatus;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import de.medizininformatik_initiative.processes.common.util.ConstantsBase;
import dev.dsf.bpe.v2.constants.CodeSystems;
import dev.dsf.bpe.v2.constants.NamingSystems;
import dev.dsf.fhir.validation.ResourceValidator;
import dev.dsf.fhir.validation.ResourceValidatorImpl;
import dev.dsf.fhir.validation.ValidationSupportRule;

public class TaskProfileTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskProfileTest.class);

    private static final StoreControllerProcessPluginDefinition definition = new StoreControllerProcessPluginDefinition();

    @ClassRule
    public static final ValidationSupportRule validationRule = new ValidationSupportRule(
            definition.getResourceVersion(), definition.getReleaseDate(),
            List.of("dsf-task-2.0.0.xml", "task-create-store.xml",
                    "task-delete-store.xml", "task-created-store.xml"),
            List.of("dsf-read-access-tag-2.0.0.xml", "dsf-bpmn-message-2.0.0.xml", "dmu-tools.xml", "data-sharing.xml",
                    "mii-data-set-status.xml"),
            List.of("dsf-read-access-tag-2.0.0.xml", "dsf-bpmn-message-2.0.0.xml", "dmu-tools.xml", "data-sharing.xml",
                     "mii-data-set-status-receive.xml", "mii-data-set-status-send.xml"));

    private final ResourceValidator resourceValidator = new ResourceValidatorImpl(validationRule.getFhirContext(),
            validationRule.getValidationSupport());

    @Test
    public void testTaskCreateStore()
    {
        Task task = createValidTaskCreateStore();

        ValidationResult result = resourceValidator.validate(task);
        ValidationSupportRule.logValidationMessages(logger, result);

        assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
                || ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
    }


    private Task createValidTaskCreateStore()
    {
        Task task = new Task();
        task.getMeta()
                .addProfile(StoreControllerConstants.PROFILE_TASK_CREATE_STORE + "|" + definition.getResourceVersion());
        task.setInstantiatesCanonical(
                StoreControllerConstants.PROFILE_TASK_CREATE_STORE_PROCESS_URI + "|" + definition.getResourceVersion());
        task.setStatus(TaskStatus.REQUESTED);
        task.setIntent(TaskIntent.ORDER);
        task.setAuthoredOn(new Date());
        task.getRequester().setType(ResourceType.Organization.name())
                .setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DIC"));
        task.getRestriction().addRecipient().setType(ResourceType.Organization.name())
                .setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DIC"));
        task.addInput().setValue(new StringType(StoreControllerConstants.MESSAGE_NAME_CREATE))
                .getType().addCoding(CodeSystems.BpmnMessage.messageName());

        task.addInput().setValue(new StringType("test"))
                .getType().addCoding(CodeSystems.BpmnMessage.businessKey());


        task.addInput()
                .setValue(new Identifier().setSystem(ConstantsBase.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER)
                        .setValue("Test_PROJECT"))
                .getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DATA_SHARING)
                .setVersion(definition.getResourceVersion())
                .setCode(StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER);

        return task;
    }

    @Test
    public void testTaskDeleteStore()
    {
        Task task = createValidTaskDeleteStore();

        ValidationResult result = resourceValidator.validate(task);
        ValidationSupportRule.logValidationMessages(logger, result);

        assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
                || ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
    }

    private Task createValidTaskDeleteStore()
    {
        Task task = new Task();
        task.getMeta().addProfile(StoreControllerConstants.PROFILE_TASK_DELETE_STORE + "|" + definition.getResourceVersion());
        task.setInstantiatesCanonical(
                StoreControllerConstants.PROFILE_TASK_DELETE_STORE_PROCESS_URI + "|" + definition.getResourceVersion());
        task.setStatus(TaskStatus.REQUESTED);
        task.setIntent(TaskIntent.ORDER);
        task.setAuthoredOn(new Date());
        task.getRequester().setType(ResourceType.Organization.name())
                .setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DIC"));
        task.getRestriction().addRecipient().setType(ResourceType.Organization.name())
                .setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DIC"));
        task.addInput().setValue(new StringType(StoreControllerConstants.MESSAGE_NAME_DELETE)).getType()
                .addCoding(CodeSystems.BpmnMessage.messageName());
        task.addInput().setValue(new StringType("test"))
                .getType().addCoding(CodeSystems.BpmnMessage.businessKey());


        task.addInput()
                .setValue(new Identifier().setSystem(ConstantsBase.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER)
                        .setValue("Test_PROJECT"))
                .getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DATA_SHARING)
                .setVersion(definition.getResourceVersion())
                .setCode(StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER);

        return task;
    }

    @Test
    public void testTaskStoreCreate()
    {
        Task task = createValidTaskCreatedStore();

        ValidationResult result = resourceValidator.validate(task);
        ValidationSupportRule.logValidationMessages(logger, result);

        assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
                || ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
    }


    private Task createValidTaskCreatedStore()
    {
        Task task = new Task();
        task.getMeta().addProfile(StoreControllerConstants.PROFILE_TASK_CREATED_STORE + "|" + definition.getResourceVersion());
        task.setInstantiatesCanonical(
                StoreControllerConstants.PROFILE_TASK_CREATE_STORE_PROCESS_URI + "|" + definition.getResourceVersion());
        task.setStatus(TaskStatus.REQUESTED);
        task.setIntent(TaskIntent.ORDER);
        task.setAuthoredOn(new Date());
        task.getRequester().setType(ResourceType.Organization.name())
                .setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DIC"));
        task.getRestriction().addRecipient().setType(ResourceType.Organization.name())
                .setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DIC"));
        task.addInput().setValue(new StringType(StoreControllerConstants.MESSAGE_NAME_CREATED)).getType()
                .addCoding(CodeSystems.BpmnMessage.messageName());
        task.addInput().setValue(new StringType("test"))
                .getType().addCoding(CodeSystems.BpmnMessage.businessKey());

        task.addInput()
                .setValue(new Identifier().setSystem(ConstantsBase.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER)
                        .setValue("Test_PROJECT"))
                .getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DATA_SHARING)
                .setVersion(definition.getResourceVersion())
                .setCode(StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER);


        task.addInput()
                .setValue(new UrlType("https://your-store-url-here"))
                .getType().addCoding()
                .setSystem(StoreControllerConstants.CODESYSTEM_DMU_TOOLS)
                .setCode(StoreControllerConstants.CODESYSTEM_DMU_VALUE_STORE_URL);

        return task;
    }

}
