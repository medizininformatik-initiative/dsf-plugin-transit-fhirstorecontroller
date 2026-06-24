package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.ServiceTask;
import dev.dsf.bpe.v2.variables.Variables;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDockerStore implements ServiceTask
{
	private static final Logger logger = LoggerFactory.getLogger(CreateDockerStore.class);

	@Override
	public void execute(ProcessPluginApi api, Variables variables)
	{
		Task task = variables.getStartTask();

		String projectIdentifier = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER);

		String bussinessKey = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY);

		logger.info(
				"Create store in docker engine of approved data sharing project [project-identifier: {}; task-id: {}]",
				projectIdentifier,
				task.getId());
	}
}
