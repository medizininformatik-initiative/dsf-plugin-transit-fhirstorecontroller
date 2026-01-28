package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.medizininformatik_initiative.processes.common.util.ConstantsBase;
import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.constants.CodeSystems;
import dev.dsf.bpe.v1.variables.Target;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrepareStoreCreation extends AbstractServiceDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(PrepareStoreCreation.class);

	public PrepareStoreCreation(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected void doExecute(DelegateExecution execution, Variables variables)
	{
		Task task = variables.getStartTask();

		String projectIdentifier = getProjectIdentifier(task);
		variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER, projectIdentifier);

		String bussinessKey = getBussinessKey(task);
		variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY, bussinessKey);

		variables.setTarget(getDmsTarget(variables));

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

	private String getBussinessKey(Task task)
	{
		return api.getTaskHelper()
				.getFirstInputParameterValue(task, CodeSystems.BpmnMessage.businessKey(), StringType.class)
				.orElseThrow(() -> new RuntimeException("Business Key is missing")).getValue();
	}
	private Target getDmsTarget(Variables variables)
	{
		return variables.createTarget(api.getOrganizationProvider().getLocalOrganizationIdentifierValue().get(),
				api.getOrganizationProvider().getLocalOrganization().get().getName(),
				api.getEndpointProvider().getLocalEndpointAddress());
	}


}
