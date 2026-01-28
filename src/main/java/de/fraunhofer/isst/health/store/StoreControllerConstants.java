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

	int TIMEOUT_MS = 10000;
}
