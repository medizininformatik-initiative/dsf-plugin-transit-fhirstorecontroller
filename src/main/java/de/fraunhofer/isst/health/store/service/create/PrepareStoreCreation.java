package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.spring.config.StoreVariablesConfig;
import de.medizininformatik_initiative.processes.common.util.ConstantsBase;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.ServiceTask;
import dev.dsf.bpe.v2.constants.CodeSystems;
import dev.dsf.bpe.v2.variables.Variables;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrepareStoreCreation implements ServiceTask
{
	private static final Logger logger = LoggerFactory.getLogger(PrepareStoreCreation.class);

	private final StoreVariablesConfig storeVariablesConfig;

	public PrepareStoreCreation(StoreVariablesConfig storeVariablesConfig) {
		this.storeVariablesConfig = storeVariablesConfig;
	}

	@Override
	public void execute(ProcessPluginApi api, Variables variables)
	{
		Task task = variables.getStartTask();

		String projectIdentifier = getProjectIdentifier(task);
		variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER, projectIdentifier);

		String bussinessKey = getBussinessKey(task, api);
		variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY, bussinessKey);

		variables.setBoolean(StoreControllerConstants.BPMN_EXECUTION_KUBERNETES, storeVariablesConfig.isKubernetes());

		//DMS Target for Store controller
		variables.setTarget(
				variables.createTarget(api.getOrganizationProvider().getLocalOrganizationIdentifierValue().get(),
						api.getEndpointProvider().getLocalEndpointIdentifierValue().get(),
						api.getEndpointProvider().getLocalEndpointAddress()));

		logger.info(
				"Starting store creation of approved data sharing project [project-identifier: {}; task-id: {}]",
				projectIdentifier,
				task.getId());

	}

	private String getProjectIdentifier(Task task)
	{
		return task.getInput().stream().filter(i -> i.getType().getCoding().stream()
				.anyMatch(c -> StoreControllerConstants.CODESYSTEM_DATA_SHARING.equals(c.getSystem())
						&& StoreControllerConstants.CODESYSTEM_DATA_SHARING_VALUE_PROJECT_IDENTIFIER.equals(c.getCode())))
				.filter(i -> i.getValue() instanceof Identifier).map(i -> (Identifier) i.getValue())
				.filter(i -> ConstantsBase.NAMINGSYSTEM_MII_PROJECT_IDENTIFIER.equals(i.getSystem()))
				.map(Identifier::getValue).map(String::trim).findFirst().orElseThrow(() -> new RuntimeException(
						"No project-identifier present in task with id '" + task.getId() + "'"));
	}

	private String getBussinessKey(Task task, ProcessPluginApi api)
	{
		return api.getTaskHelper()
				.getFirstInputParameterValue(task, CodeSystems.BpmnMessage.businessKey(), StringType.class)
				.orElseThrow(() -> new RuntimeException("Business Key is missing")).getValue();
	}
}
