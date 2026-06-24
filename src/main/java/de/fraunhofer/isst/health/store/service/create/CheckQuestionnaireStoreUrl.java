package de.fraunhofer.isst.health.store.service.create;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.medizininformatik_initiative.processes.common.util.ConstantsBase;
import dev.dsf.bpe.v2.ProcessPluginApi;
import dev.dsf.bpe.v2.activity.ServiceTask;
import dev.dsf.bpe.v2.error.ErrorBoundaryEvent;
import dev.dsf.bpe.v2.variables.Variables;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CheckQuestionnaireStoreUrl implements ServiceTask {
    private static final Logger logger = LoggerFactory.getLogger(CheckQuestionnaireStoreUrl.class);

    @Override
    public void execute(ProcessPluginApi api, Variables variables) throws ErrorBoundaryEvent, Exception {
        Task task = variables.getStartTask();
        String projectIdentifier = variables.getString(StoreControllerConstants.BPMN_EXECUTION_VARIABLE_PROJECT_IDENTIFIER);
        QuestionnaireResponse questionnaireResponse = variables.getLatestReceivedQuestionnaireResponse();

        Optional<String> storeUrlOptioonal = getStoreUrl(questionnaireResponse);

        if (projectIdentifierMatch(questionnaireResponse, projectIdentifier) && storeUrlOptioonal.isPresent())
        {
            String storeUrl = storeUrlOptioonal.get();

            variables.setString(StoreControllerConstants.FHIRSTOREURL, storeUrl);

            logger.info("Store URL provided for Project '{}' in Task '{}'", projectIdentifier,
                    api.getTaskHelper().getLocalVersionlessAbsoluteUrl(task));
        }
        else
        {
            String expectedIdentifier = getProjectIdentifier(questionnaireResponse);
            logger.warn(
                    "Store URL provided for Project '{}' in Task '{}' failed - expected and provided project-identifier do not match (expected: {}, provided: {}) or merged data-set URL is not present - throwing error boundary event",
                    projectIdentifier, api.getTaskHelper().getLocalVersionlessAbsoluteUrl(task), expectedIdentifier,
                    projectIdentifier);

            String message = "Store Provision Failed" + ConstantsBase.EXCEPTION_MESSAGE_DIVIDER
                    + "project-identifiers do not match (expected: " + projectIdentifier + ", provided:"
                    + expectedIdentifier + ") or store URL not present";
            throw new ErrorBoundaryEvent(ConstantsBase.CODESYSTEM_DATA_SET_STATUS_VALUE_NOT_SENT, message);
        }
    }

    private Optional<String> getStoreUrl(QuestionnaireResponse questionnaireResponse)
    {
        return questionnaireResponse.getItem().stream()
                .filter(i -> StoreControllerConstants.QUESTIONNAIRES_ITEM_STORE_URL.equals(i.getLinkId()))
                .filter(QuestionnaireResponse.QuestionnaireResponseItemComponent::hasAnswer)
                .flatMap(i -> i.getAnswer().stream())
                .filter(QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent::hasValue)
                .map(QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent::getValue)
                .filter(a -> UriType.class.isAssignableFrom(a.getClass())).map(a -> (UriType) a)
                .filter(PrimitiveType::hasValue).map(PrimitiveType::getValue).findFirst();
    }

    private boolean projectIdentifierMatch(QuestionnaireResponse questionnaireResponse,
                                           String expectedProjectIdentifier)
    {
        return getProjectIdentifiers(questionnaireResponse).anyMatch(foundProjectIdentifier -> expectedProjectIdentifier
                .trim().equalsIgnoreCase(foundProjectIdentifier.trim()));
    }

    private String getProjectIdentifier(QuestionnaireResponse questionnaireResponse)
    {
        return getProjectIdentifiers(questionnaireResponse).findFirst().orElse("unknown");
    }

    private Stream<String> getProjectIdentifiers(QuestionnaireResponse questionnaireResponse)
    {
        return questionnaireResponse.getItem().stream()
                .filter(i -> StoreControllerConstants.QUESTIONNAIRES_ITEM_RELEASE.equals(i.getLinkId()))
                .flatMap(i -> i.getAnswer().stream()).filter(a -> a.getValue() instanceof StringType)
                .map(a -> (StringType) a.getValue()).map(PrimitiveType::getValue).filter(Objects::nonNull);
    }
}
