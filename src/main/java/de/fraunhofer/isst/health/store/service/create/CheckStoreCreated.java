package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.spring.config.FhirStoreClientConfig;
import de.medizininformatik_initiative.processes.common.fhir.client.FhirClient;
import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckStoreCreated extends AbstractServiceDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(CheckStoreCreated.class);

	private final FhirStoreClientConfig fhirStoreClientConfig;

	public CheckStoreCreated(ProcessPluginApi api, FhirStoreClientConfig fhirStoreClientConfig)
	{
		super(api);
		this.fhirStoreClientConfig = fhirStoreClientConfig;
	}

	@Override
	protected void doExecute(DelegateExecution execution, Variables variables)
	{
		Task task = variables.getStartTask();

		String projectIdentifier = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER);

		String storeUrl = variables.getString(StoreControllerConstants.FHIRSTOREURL);

		logger.info(
				"Checking store creation of approved data sharing project [project-identifier: {}; task-id: {}]",
				projectIdentifier,
				task.getId());

		fhirStoreClientConfig.setFhirStoreBaseUrl(storeUrl);
		FhirClient fhirClient = fhirStoreClientConfig.fhirClientFactory().getFhirClient();
		try
		{
			fhirClient.testConnection();
			variables.setString(StoreControllerConstants.STORE_STATUS, "created");
			logger.info(
					"Store Created for approved data sharing project [project-identifier: {}; task-id: {}]",
					projectIdentifier,
					task.getId());
		}catch (Exception e){
			variables.setString(StoreControllerConstants.STORE_STATUS, "not-created");
		}

	}
}
