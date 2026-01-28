package de.fraunhofer.isst.health.store.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "de.fraunhofer.isst.health.store")
public class StoreVariablesConfig
{

	@Value("${eu.datamanagementunit.store.hostname:#{null}}")
	private String storeHostname;

	@Value("${eu.datamanagementunit.store.git.url:#{null}}")
	private String gitUrl;

	@Value("${eu.datamanagementunit.store.git.branch:#{null}}")
	private String gitBranch;

	@Value("${eu.datamanagementunit.store.git.username:#{null}}")
	private String gitUsername;

	@Value("${eu.datamanagementunit.store.git.credentials:#{null}}")
	private String gitCredentials;


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
}
