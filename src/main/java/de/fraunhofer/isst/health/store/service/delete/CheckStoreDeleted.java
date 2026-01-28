package de.fraunhofer.isst.health.store.service.delete;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.spring.config.FhirStoreClientConfig;
import de.medizininformatik_initiative.processes.common.fhir.client.FhirClient;
import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckStoreDeleted extends AbstractServiceDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(CheckStoreDeleted.class);
    private final FhirStoreClientConfig fhirStoreClientConfig;

    public CheckStoreDeleted(ProcessPluginApi api, FhirStoreClientConfig fhirStoreClientConfig) {
        super(api);
        this.fhirStoreClientConfig = fhirStoreClientConfig;
    }

    @Override
    protected void doExecute(DelegateExecution delegateExecution, Variables variables) throws BpmnError {
        Task task = variables.getStartTask();
        String projectIdentifier = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER);
        String storeUrl = variables.getString(StoreControllerConstants.FHIRSTOREURL);

        logger.info(
                "Checking store deletion of data sharing project [project-identifier: {}; task-id: {}]",
                projectIdentifier,
                task.getId());

        fhirStoreClientConfig.setFhirStoreBaseUrl(storeUrl);
        FhirClient fhirClient = fhirStoreClientConfig.fhirClientFactory().getFhirClient();
        try
        {
            fhirClient.testConnection();
        }catch (Exception e){
            variables.setString(StoreControllerConstants.STORE_STATUS, "deleted");
        }
    }
}
