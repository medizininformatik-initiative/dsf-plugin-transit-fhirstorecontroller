package de.fraunhofer.isst.health.store;

public interface StoreControllerConstants
{
	String PROCESS_TRANSIT_NAME_BASE = "datamanagementuniteu_";

	String PROCESS_NAME_STORE_CONTROLLER_CREATE = "storeControllerCreate";

	String PROCESS_NAME_STORE_CONTROLLER_DELETE = "storeControllerDelete";

	String PROCESS_NAME_FULL_STORE_CONTROLLER_CREATE = PROCESS_TRANSIT_NAME_BASE + PROCESS_NAME_STORE_CONTROLLER_CREATE;

	String PROCESS_NAME_FULL_STORE_CONTROLLER_DELETE = PROCESS_TRANSIT_NAME_BASE + PROCESS_NAME_STORE_CONTROLLER_DELETE;

	String BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER = "projectIdentifier";
	String BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY = "contractUrl";

	String CODESYSTEM_DATA_SHARING = "http://medizininformatik-initiative.de/fhir/CodeSystem/data-sharing";
	String CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER = "project-identifier";

	//MII-DMU_TOOLS
	String CODESYSTEM_DMU_TOOLS = "http://datamanagementunit.eu/fhir/CodeSystem/dmu-tools";
	String CODESYSTEM_DMU_VALUE_STORE_URL = "store-url";

	String FHIRSTOREURL = "fhirContainerUrl";

	String STORE_STATUS = "store-status";

	String BPMN_EXECUTION_VARIABLE_CONSORTIUM_IDENTIFIER = "consortium-identifie";

	// Message names
	String MESSAGE_NAME_CREATE = "createFhirStore";
	String MESSAGE_NAME_CREATED = "createdStore";
	String MESSAGE_NAME_DELETE = "deleteFhirStore";

	// Naming system for project identifier
	String NAMINGSYSTEM_MII_PROJECT_IDENTIFIER = "http://medizininformatik-initiative.de/sid/project-identifier";

	// FHIR profiles (base URLs without version)
	String PROFILE_TASK_CREATE_STORE = "http://datamanagementunit.eu/fhir/StructureDefinition/task-create-store";
	String PROFILE_TASK_CREATED_STORE = "http://datamanagementunit.eu/fhir/StructureDefinition/task-created-store";
	String PROFILE_TASK_DELETE_STORE = "http://datamanagementunit.eu/fhir/StructureDefinition/task-delete-store";

	//FHIR canonical
	String PROFILE_TASK_CREATE_STORE_PROCESS_URI = "http://datamanagementunit.eu/bpe/Process/storeControllerCreate";
	String PROFILE_TASK_DELETE_STORE_PROCESS_URI = "http://datamanagementunit.eu/bpe/Process/storeControllerDelete";

	int TIMEOUT_MS = 10000;
}
