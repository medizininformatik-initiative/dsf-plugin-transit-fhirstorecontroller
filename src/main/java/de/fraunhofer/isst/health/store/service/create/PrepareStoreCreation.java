package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.medizininformatik_initiative.processes.common.util.ConstantsBase;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.ServiceTask;
import dev.dsf.bpe.v2.constants.CodeSystems;
import dev.dsf.bpe.v2.constants.NamingSystems;
import dev.dsf.bpe.v2.variables.Target;
import dev.dsf.bpe.v2.variables.Variables;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrepareStoreCreation implements ServiceTask
{
	private static final Logger logger = LoggerFactory.getLogger(PrepareStoreCreation.class);

	@Override
	public void execute(ProcessPluginApi api, Variables variables)
	{
		Task task = variables.getStartTask();

		String projectIdentifier = getProjectIdentifier(task);
		variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER, projectIdentifier);

		String bussinessKey = getBussinessKey(task, api);
		variables.setString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_BUSSINESS_KEY, bussinessKey);

		String consortiumIdentifier = "Parent_Organization";
        //String consortiumIdentifier = variables
		// .getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_CONSORTIUM_IDENTIFIER);


		Identifier dicIdentifier = getDicOrganizationIdentifier(task);
		Endpoint dicEndpoint = getDicEndpoint(api, consortiumIdentifier, dicIdentifier);

		Target dicTarget = createTarget(variables, dicIdentifier, dicEndpoint);
		variables.setTarget(dicTarget);

		logger.info(
				"Starting store creation of approved data sharing project [project-identifier: {}; task-id: {}]",
				projectIdentifier,
				task.getId());

	}

	private Identifier getDicOrganizationIdentifier(Task task)
	{
		return task.getRequester().getIdentifier();
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

	private Endpoint getDicEndpoint(ProcessPluginApi api, String consortiumIdentifier, Identifier dicIdentifier)
	{
		Identifier parentIdentifier = NamingSystems.OrganizationIdentifier.withValue(consortiumIdentifier);
		Coding role = new Coding().setSystem(ConstantsBase.CODESYSTEM_DSF_ORGANIZATION_ROLE)
				.setCode(ConstantsBase.CODESYSTEM_DSF_ORGANIZATION_ROLE_VALUE_DIC);
		return api.getEndpointProvider().getEndpoint(parentIdentifier, dicIdentifier, role)
				.orElseThrow(() -> new RuntimeException(
						"Could not find default endpoint of organization '" + dicIdentifier.getValue() + "'"));
	}

	private Target createTarget(Variables variables, Identifier dicIdentifier, Endpoint dicEndpoint)
	{
		String dicEndpointIdentifier = extractEndpointIdentifier(dicEndpoint);
		return variables.createTarget(dicIdentifier.getValue(), dicEndpointIdentifier, dicEndpoint.getAddress());
	}

	private String extractEndpointIdentifier(Endpoint endpoint)
	{
		return endpoint.getIdentifier().stream().filter(i -> NamingSystems.EndpointIdentifier.SID.equals(i.getSystem()))
				.map(Identifier::getValue).findFirst()
				.orElseThrow(() -> new RuntimeException("Endpoint with id '" + endpoint.getId()
						+ "' is missing identifier with system '" + NamingSystems.EndpointIdentifier.SID + "'"));
	}

	private String getBussinessKey(Task task, ProcessPluginApi api)
	{
		return api.getTaskHelper()
				.getFirstInputParameterValue(task, CodeSystems.BpmnMessage.businessKey(), StringType.class)
				.orElseThrow(() -> new RuntimeException("Business Key is missing")).getValue();
	}
}
