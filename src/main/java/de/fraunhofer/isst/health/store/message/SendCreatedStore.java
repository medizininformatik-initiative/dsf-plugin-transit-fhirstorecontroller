package de.fraunhofer.isst.health.store.message;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.medizininformatik_initiative.processes.common.activity.RetryTaskSender;
import de.medizininformatik_initiative.processes.common.util.ConstantsBase;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.MessageSendTask;
import dev.dsf.bpe.v2.activity.task.BusinessKeyStrategies;
import dev.dsf.bpe.v2.activity.task.TaskSender;
import dev.dsf.bpe.v2.activity.values.SendTaskValues;
import dev.dsf.bpe.v2.error.MessageSendTaskErrorHandler;
import dev.dsf.bpe.v2.error.impl.ExceptionToErrorBoundaryEventTranslationErrorHandler;
import dev.dsf.bpe.v2.variables.Target;
import dev.dsf.bpe.v2.variables.Variables;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.UrlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SendCreatedStore implements MessageSendTask
{
	private static final Logger logger = LoggerFactory.getLogger(SendCreatedStore.class);

	@Override
	public List<Task.ParameterComponent> getAdditionalInputParameters(ProcessPluginApi api, Variables variables,
			SendTaskValues sendTaskValues, Target target)
	{
		List<Task.ParameterComponent> parameters = new ArrayList<>();

		String projectIdentifier = variables
				.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER);
		Task.ParameterComponent projectIdentifierInput = getProjectIdentifierInput(projectIdentifier);
		parameters.add(projectIdentifierInput);

		String storeUrl = variables.getString(StoreControllerConstants.FHIRSTOREURL);
		Task.ParameterComponent contractUrlInput = getStoreUrlInput(storeUrl);
		parameters.add(contractUrlInput);

		return parameters;
	}

	@Override
	public TaskSender getTaskSender(ProcessPluginApi api, Variables variables, SendTaskValues sendTaskValues)
	{
		return new RetryTaskSender(api, variables, sendTaskValues, BusinessKeyStrategies.SAME,
				(target) -> getAdditionalInputParameters(api, variables, sendTaskValues, target));
	}

	@Override
	public MessageSendTaskErrorHandler getErrorHandler()
	{
		return new ExceptionToErrorBoundaryEventTranslationErrorHandler(
				exception -> "SEND_ERROR",
				exception -> "SendCreatedStore failed: " + exception.getMessage()
		);
	}

	private Task.ParameterComponent getProjectIdentifierInput(String projectIdentifier)
	{
		Task.ParameterComponent projectIdentifierInput = new Task.ParameterComponent();
		projectIdentifierInput.getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DATA_SHARING)
				.setCode(StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER);
		projectIdentifierInput.setValue(new Identifier().setSystem(ConstantsBase.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER)
				.setValue(projectIdentifier));

		return projectIdentifierInput;
	}

	private Task.ParameterComponent getStoreUrlInput(String storeUrl)
	{
		Task.ParameterComponent contractUrlInput = new Task.ParameterComponent();
		contractUrlInput.getType().addCoding().setSystem(StoreControllerConstants.CODESYSTEM_DMU_TOOLS)
				.setCode(StoreControllerConstants.CODESYSTEM_DMU_VALUE_STORE_URL);
		contractUrlInput.setValue(new UrlType(storeUrl));

		return contractUrlInput;
	}
}
