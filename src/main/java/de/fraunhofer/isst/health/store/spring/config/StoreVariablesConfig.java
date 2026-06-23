package de.fraunhofer.isst.health.store.spring.config;

import dev.dsf.bpe.v2.documentation.ProcessDocumentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "de.fraunhofer.isst.health.store")
public class StoreVariablesConfig
{
	@ProcessDocumentation(required = true,processNames = {
			"datamanagementuniteu_storeControllerCreate" },
			description = "Base URL the store controllers share", example = "http://dmu-fhir-store")
	@Value("${eu.datamanagementunit.store.hostname:#{null}}")
	private String storeHostname;

	@ProcessDocumentation(required = true,processNames = {
			"datamanagementuniteu_storeControllerCreate" },
			description = "Base URL of the git repository that holds the Kubernetes Helm Charts",
			example = "https://github/dmu-argocd-charts.git")
	@Value("${eu.datamanagementunit.store.git.url:#{null}}")
	private String gitUrl;

	@ProcessDocumentation(required = true,processNames = {
			"datamanagementuniteu_storeControllerCreate" },
			description = "Git Branch that should be pulled",
			example = "main")
	@Value("${eu.datamanagementunit.store.git.branch:#{null}}")
	private String gitBranch;

	@ProcessDocumentation(required = true,processNames = {
			"datamanagementuniteu_storeControllerCreate" },
			description = "Git User with access to the repository",
			example = "main")
	@Value("${eu.datamanagementunit.store.git.username:#{null}}")
	private String gitUsername;

	@ProcessDocumentation(required = true,processNames = {
			"datamanagementuniteu_storeControllerCreate" },
			description = "Git User password",
			example = "main")
	@Value("${eu.datamanagementunit.store.git.credentials:#{null}}")
	private String gitCredentials;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" },
			description = "Kubernetes Deployment",
			example = "false")
	@Value("${eu.datamanagementunit.store.kubernetes:false}")
	private boolean kubernetes;

	@ProcessDocumentation(processNames = {
			"medizininformatik-initiativede_mergeDataSharing" }, description = "To receive e-mails as DMS, set to `true`")
	@Value("${de.medizininformatik.initiative.data.sharing.dms.email.enabled:false}")
	private boolean dmsEmailEnabled;


	public String getStoreHostname()
	{
		return storeHostname;
	}

	public String getGitBranch()
	{
		return gitBranch;
	}

	public String getGitUrl()
	{
		return gitUrl;
	}

	public String getGitUsername()
	{
		return gitUsername;
	}

	public String getGitCredentials()
	{
		return gitCredentials;
	}

	public boolean isDmsEmailEnabled() {
		return dmsEmailEnabled;
	}

	public boolean isKubernetes() {
		return kubernetes;
	}
}
