package de.fraunhofer.isst.health.store.spring.config;


import de.fraunhofer.isst.health.store.message.SendCreatedStore;
import de.fraunhofer.isst.health.store.service.create.CheckStoreCreated;
import de.fraunhofer.isst.health.store.service.create.CreateKubernetesChart;
import de.fraunhofer.isst.health.store.service.create.PrepareStoreCreation;
import de.fraunhofer.isst.health.store.service.delete.CheckStoreDeleted;
import de.fraunhofer.isst.health.store.service.delete.DeleteStore;
import de.medizininformatik_initiative.processes.common.mimetype.CombinedDetectors;
import de.medizininformatik_initiative.processes.common.mimetype.MimeTypeHelper;
import dev.dsf.bpe.v1.ProcessPluginApi;
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
	private ProcessPluginApi api;

	@Autowired
	private StoreVariablesConfig storeVariablesConfig;

	@Autowired
	private FhirStoreClientConfig fhirStoreClientConfig;

	// all Processes

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public MimeTypeHelper mimeTypeHelper()
	{
		return new MimeTypeHelper(CombinedDetectors.fromDefaultWithNdJson(), api.getFhirContext());
	}

	//Store Process
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public PrepareStoreCreation prepareStoreCreation()
	{
		return new PrepareStoreCreation(api);
	}
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CheckStoreCreated checkStoreCreated()
	{
		return new CheckStoreCreated(api, fhirStoreClientConfig);
	}
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CreateKubernetesChart createKubernetesChart()
	{
		return new CreateKubernetesChart(api, storeVariablesConfig);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SendCreatedStore sendCreatedStore()
	{
		return new SendCreatedStore(api);
	}

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DeleteStore deleteStore()
    {
        return new DeleteStore(api, storeVariablesConfig);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CheckStoreDeleted checkStoreDeleted()
    {
        return new CheckStoreDeleted(api, fhirStoreClientConfig);
    }
}
