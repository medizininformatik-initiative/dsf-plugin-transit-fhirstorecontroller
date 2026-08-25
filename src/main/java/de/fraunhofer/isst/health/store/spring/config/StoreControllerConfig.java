package de.fraunhofer.isst.health.store.spring.config;

import de.fraunhofer.isst.health.store.message.SendCreatedStore;
import de.fraunhofer.isst.health.store.questionnaire.ConfirmStoreListener;
import de.fraunhofer.isst.health.store.service.create.CheckQuestionnaireStoreUrl;
import de.fraunhofer.isst.health.store.service.create.CheckStoreCreated;
import de.fraunhofer.isst.health.store.service.create.CreateKubernetesChart;
import de.fraunhofer.isst.health.store.service.create.PrepareStoreCreation;
import de.fraunhofer.isst.health.store.service.delete.CheckStoreDeleted;
import de.fraunhofer.isst.health.store.service.delete.DeleteStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "de.fraunhofer.isst.health.store")
public class StoreControllerConfig
{
	@Autowired
	private StoreVariablesConfig storeVariablesConfig;

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public PrepareStoreCreation prepareStoreCreation()
	{
		return new PrepareStoreCreation(storeVariablesConfig);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CheckStoreCreated checkStoreCreated()
	{
		return new CheckStoreCreated();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CreateKubernetesChart createKubernetesChart()
	{
		return new CreateKubernetesChart(storeVariablesConfig);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SendCreatedStore sendCreatedStore()
	{
		return new SendCreatedStore();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DeleteStore deleteStore()
	{
		return new DeleteStore(storeVariablesConfig);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CheckStoreDeleted checkStoreDeleted()
	{
		return new CheckStoreDeleted();
	}

	//Questionnare Handler
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ConfirmStoreListener confirmStoreListener()
	{
		return new ConfirmStoreListener(storeVariablesConfig.isDmsEmailEnabled());
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CheckQuestionnaireStoreUrl checkQuestionnaireStoreUrl()
	{
		return new CheckQuestionnaireStoreUrl();
	}
}
