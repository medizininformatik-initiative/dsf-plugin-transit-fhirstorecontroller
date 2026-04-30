package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.spring.config.StoreVariablesConfig;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.ServiceTask;
import dev.dsf.bpe.v2.client.dsf.DsfClient;
import dev.dsf.bpe.v2.service.DsfClientProvider;
import dev.dsf.bpe.v2.variables.Variables;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckStoreCreated implements ServiceTask
{
	private static final Logger logger = LoggerFactory.getLogger(CheckStoreCreated.class);

	private final StoreVariablesConfig storeVariablesConfig;

	public CheckStoreCreated(StoreVariablesConfig storeVariablesConfig)
	{
		this.storeVariablesConfig = storeVariablesConfig;
	}

	@Override
	public void execute(ProcessPluginApi api, Variables variables)
	{
		Task task = variables.getStartTask();

		String projectIdentifier = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER);

		String storeUrl = variables.getString(StoreControllerConstants.FHIRSTOREURL);

		logger.info(
				"Checking store creation of approved data sharing project [project-identifier: {}; task-id: {}]",
				projectIdentifier,
				task.getId());

		DsfClient client = getDsfClientForFhirStore(api.getDsfClientProvider(), storeUrl);
		try
		{
			client.getConformance();
			variables.setString(StoreControllerConstants.STORE_STATUS, "created");
			logger.info(
					"Store Created for approved data sharing project [project-identifier: {}; task-id: {}]",
					projectIdentifier,
					task.getId());
		}catch (Exception e){
			variables.setString(StoreControllerConstants.STORE_STATUS, "not-created");
		}
	}

	private DsfClient getDsfClientForFhirStore(DsfClientProvider provider, String storeUrl)
	{
		return provider.getByEndpointUrl(storeUrl);
	}
}
