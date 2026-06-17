//package de.fraunhofer.isst.health.store.fhir.profile;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.Date;
//import java.util.List;
//
//import org.hl7.fhir.r4.model.Identifier;
//import org.hl7.fhir.r4.model.StringType;
//import org.hl7.fhir.r4.model.Task;
//import org.hl7.fhir.r4.model.Task.TaskIntent;
//import org.hl7.fhir.r4.model.Task.TaskStatus;
//import org.junit.ClassRule;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import ca.uhn.fhir.validation.ResultSeverityEnum;
//import ca.uhn.fhir.validation.ValidationResult;
//import de.fraunhofer.isst.health.store.StoreControllerConstants;
//import de.fraunhofer.isst.health.store.StoreControllerProcessPluginDefinition;
//import dev.dsf.bpe.v2.constants.CodeSystems;
//import dev.dsf.bpe.v2.constants.NamingSystems;
//import dev.dsf.fhir.validation.ResourceValidator;
//import dev.dsf.fhir.validation.ResourceValidatorImpl;
//import dev.dsf.fhir.validation.ValidationSupportRule;
//
//public class TaskProfileTest
//{
//	private static final Logger logger = LoggerFactory.getLogger(TaskProfileTest.class);
//
//	private static final StoreControllerProcessPluginDefinition definition = new StoreControllerProcessPluginDefinition();
//
//	@ClassRule
//	public static final ValidationSupportRule validationRule = new ValidationSupportRule(
//			definition.getResourceVersion(), definition.getReleaseDate(),
//			List.of("dsf-task-2.0.0.xml", "task-create-store.xml", "task-created-store.xml", "task-delete-store.xml"),
//			List.of("dsf-read-access-tag-2.0.0.xml", "dsf-bpmn-message-2.0.0.xml", "data-sharing.xml", "dmu-tools.xml"),
//			List.of("dsf-read-access-tag-2.0.0.xml", "dsf-bpmn-message-2.0.0.xml", "data-sharing.xml", "dmu-tools.xml"));
//
//	private final ResourceValidator resourceValidator = new ResourceValidatorImpl(validationRule.getFhirContext(),
//			validationRule.getValidationSupport());
//
//	@Test
//	public void testTaskCreateStoreValid()
//	{
//		Task task = createValidTaskCreateStore();
//
//		ValidationResult result = resourceValidator.validate(task);
//		ValidationSupportRule.logValidationMessages(logger, result);
//
//		assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
//				|| ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
//	}
//
//	@Test
//	public void testTaskCreatedStoreValid()
//	{
//		Task task = createValidTaskCreatedStore();
//
//		ValidationResult result = resourceValidator.validate(task);
//		ValidationSupportRule.logValidationMessages(logger, result);
//
//		assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
//				|| ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
//	}
//
//	@Test
//	public void testTaskDeleteStoreValid()
//	{
//		Task task = createValidTaskDeleteStore();
//
//		ValidationResult result = resourceValidator.validate(task);
//		ValidationSupportRule.logValidationMessages(logger, result);
//
//		assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
//				|| ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
//	}
//
//	private Task createValidTaskCreateStore()
//	{
//		Task task = new Task();
//		task.getMeta().addProfile(StoreControllerConstants.PROFILE_TASK_CREATE_STORE + "|" + definition.getResourceVersion());
//		task.setInstantiatesCanonical(
//				StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_CREATE + "|" + definition.getResourceVersion());
//		task.setStatus(TaskStatus.REQUESTED);
//		task.setIntent(TaskIntent.ORDER);
//		task.setAuthoredOn(new Date());
//		task.getRequester().setType("Organization")
//				.setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DMS"));
//		task.getRestriction().addRecipient().setType("Organization")
//				.setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DMS"));
//		task.addInput().setValue(new StringType(StoreControllerConstants.MESSAGE_NAME_CREATE))
//				.getType().addCoding(CodeSystems.BpmnMessage.messageName());
//
//		task.addInput().setValue(new StringType(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY))
//				.getType().addCoding(CodeSystems.BpmnMessage.businessKey());
//
//		task.addInput()
//				.setValue(new Identifier().setSystem(StoreControllerConstants.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER)
//						.setValue("Test_PROJECT"))
//				.getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DATA_SHARING)
//				.setVersion(definition.getResourceVersion())
//				.setCode(StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER);
//
//		return task;
//	}
//
//	private Task createValidTaskCreatedStore()
//	{
//		Task task = new Task();
//		task.getMeta().addProfile(StoreControllerConstants.PROFILE_TASK_CREATED_STORE + "|" + definition.getResourceVersion());
//		task.setInstantiatesCanonical(
//				StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_CREATE + "|" + definition.getResourceVersion());
//		task.setStatus(TaskStatus.REQUESTED);
//		task.setIntent(TaskIntent.ORDER);
//		task.setAuthoredOn(new Date());
//		task.getRequester().setType("Organization")
//				.setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DMS"));
//		task.getRestriction().addRecipient().setType("Organization")
//				.setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DMS"));
//		task.addInput().setValue(new StringType(StoreControllerConstants.MESSAGE_NAME_CREATED))
//				.getType().addCoding(CodeSystems.BpmnMessage.messageName());
//
//		task.addInput().setValue(new StringType(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY))
//				.getType().addCoding(CodeSystems.BpmnMessage.businessKey());
//
//		task.addInput()
//				.setValue(new Identifier().setSystem(StoreControllerConstants.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER)
//						.setValue("Test_PROJECT"))
//				.getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DATA_SHARING)
//				.setVersion(definition.getResourceVersion())
//				.setCode(StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER);
//
//		task.addInput()
//				.setValue(new Identifier().setSystem(StoreControllerConstants.CODESYSTEM_DMU_TOOLS))
//				.getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DMU_TOOLS)
//				.setVersion(definition.getResourceVersion())
//				.setCode(StoreControllerConstants.CODESYSTEM_DMU_VALUE_STORE_URL);
//
//		return task;
//	}
//
//	private Task createValidTaskDeleteStore()
//	{
//		Task task = new Task();
//		task.getMeta().addProfile(StoreControllerConstants.PROFILE_TASK_DELETE_STORE + "|" + definition.getResourceVersion());
//		task.setInstantiatesCanonical(
//				StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_DELETE + "|" + definition.getResourceVersion());
//		task.setStatus(TaskStatus.REQUESTED);
//		task.setIntent(TaskIntent.ORDER);
//		task.setAuthoredOn(new Date());
//		task.getRequester().setType("Organization")
//				.setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DMS"));
//		task.getRestriction().addRecipient().setType("Organization")
//				.setIdentifier(NamingSystems.OrganizationIdentifier.withValue("Test_DMS"));
//		task.addInput().setValue(new StringType(StoreControllerConstants.MESSAGE_NAME_DELETE))
//				.getType().addCoding(CodeSystems.BpmnMessage.messageName());
//
//		task.addInput().setValue(new StringType(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY))
//				.getType().addCoding(CodeSystems.BpmnMessage.businessKey());
//
//		task.addInput()
//				.setValue(new Identifier().setSystem(StoreControllerConstants.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER)
//						.setValue("Test_PROJECT"))
//				.getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DATA_SHARING)
//				.setVersion(definition.getResourceVersion())
//				.setCode(StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER);
//
//		return task;
//	}
//}
