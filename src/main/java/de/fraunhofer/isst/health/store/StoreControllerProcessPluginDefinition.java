package de.fraunhofer.isst.health.store;

import de.fraunhofer.isst.health.store.spring.config.StoreControllerConfig;
import de.fraunhofer.isst.health.store.spring.config.StoreVariablesConfig;
import dev.dsf.bpe.v2.ProcessPluginDefinition;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StoreControllerProcessPluginDefinition implements ProcessPluginDefinition
{
	public static final String VERSION = "1.0.0.2";
	public static final LocalDate RELEASE_DATE = LocalDate.of(2026, 04, 24);

	@Override
	public String getName()
	{
		return "mii-store-controller";
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public LocalDate getReleaseDate()
	{
		return RELEASE_DATE;
	}

	@Override
	public List<String> getProcessModels()
	{
		return List.of("bpe/store-controller-create.bpmn", "bpe/store-controller-delete.bpmn");
	}

	@Override
	public List<Class<?>> getSpringConfigurations()
	{
		return List.of(StoreControllerConfig.class, StoreVariablesConfig.class);
	}

	@Override
	public Map<String, List<String>> getFhirResourcesByProcessId()
	{

		//Store Controller Create
		var aStoreCreate = "fhir/ActivityDefinition/store-controller-create.xml";

		var sTcreateStore = "fhir/StructureDefinition/task-create-store.xml";
		var sTcreatedStore = "fhir/StructureDefinition/task-created-store.xml";

		//Store Controller Delete
		var aStoreDelete = "fhir/ActivityDefinition/store-controller-delete.xml";

		var sTdeleteStore = "fhir/StructureDefinition/task-delete-store.xml";

		var cDmuTools = "fhir/CodeSystem/dmu-tools.xml";
		var vDmuTools = "fhir/ValueSet/dmu-tools.xml";

		var cDaSh = "fhir/CodeSystem/data-sharing.xml";
		var vDaSh = "fhir/ValueSet/data-sharing.xml";

		return Map.of(
				StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_CREATE, //
				List.of(aStoreCreate, sTcreateStore, sTcreatedStore, cDmuTools, vDmuTools, cDaSh, vDaSh),
				StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_DELETE, //
				List.of(aStoreDelete, sTdeleteStore, cDmuTools, vDmuTools));
	}
}
