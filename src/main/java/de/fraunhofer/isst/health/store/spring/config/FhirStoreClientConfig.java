package de.fraunhofer.isst.health.store.spring.config;

import ca.uhn.fhir.context.FhirContext;
import de.medizininformatik_initiative.processes.common.fhir.client.FhirClientFactory;
import de.medizininformatik_initiative.processes.common.fhir.client.logging.DataLogger;
import de.medizininformatik_initiative.processes.common.fhir.client.token.OAuth2TokenClient;
import de.medizininformatik_initiative.processes.common.fhir.client.token.OAuth2TokenProvider;
import de.medizininformatik_initiative.processes.common.fhir.client.token.TokenClient;
import de.medizininformatik_initiative.processes.common.fhir.client.token.TokenProvider;
import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.documentation.ProcessDocumentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FhirStoreClientConfig
{
	@Autowired
	private FhirContext fhirContext;

	@Autowired
	private ProcessPluginApi api;

	@ProcessDocumentation(required = true, processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "The base address of the DMS FHIR server to read/store FHIR resources", example = "http://foo.bar/fhir")
	@Value("${eu.datamanagementunit.transit.fhir.server.base.url:#{null}}")
	private String fhirStoreBaseUrl;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "PEM encoded file with one or more trusted root certificate to validate the DMS FHIR server certificate when connecting via https", recommendation = "Use docker secret file to configure", example = "/run/secrets/hospital_ca.pem")
	@Value("${eu.datamanagementunit.transit.fhir.server.trust.certificates:#{null}}")
	private String fhirStoreTrustStore;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "PEM encoded file with client-certificate, if DMS FHIR server requires mutual TLS authentication", recommendation = "Use docker secret file to configure", example = "/run/secrets/fhir_server_client_certificate.pem")
	@Value("${eu.datamanagementunit.transit.fhir.server.certificate:#{null}}")
	private String fhirStoreCertificate;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Private key corresponding to the DMS FHIR server client-certificate as PEM encoded file. Use *${env_variable}_PASSWORD* or *${env_variable}_PASSWORD_FILE* if private key is encrypted", recommendation = "Use docker secret file to configure", example = "/run/secrets/fhir_server_private_key.pem")
	@Value("${eu.datamanagementunit.transit.fhir.server.private.key:#{null}}")
	private String fhirStorePrivateKey;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Password to decrypt the DMS FHIR server client-certificate encrypted private key", recommendation = "Use docker secret file to configure by using *${env_variable}_FILE*", example = "/run/secrets/fhir_server_private_key.pem.password")
	@Value("${eu.datamanagementunit.transit.fhir.server.private.key.password:#{null}}")
	private char[] fhirStorePrivateKeyPassword;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Basic authentication username, set if the server containing the FHIR data requests authentication using basic auth")
	@Value("${eu.datamanagementunit.transit.fhir.server.basicauth.username:#{null}}")
	private String fhirStoreUsername;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Basic authentication password, set if the server containing the FHIR data requests authentication using basic auth", recommendation = "Use docker secret file to configure by using *${env_variable}_FILE*", example = "/run/secrets/fhir_server_basicauth.password")
	@Value("${eu.datamanagementunit.transit.fhir.server.basicauth.password:#{null}}")
	private String fhirStorePassword;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Bearer token for authentication, set if the server containing the FHIR data requests authentication using a bearer token, cannot be set using docker secrets")
	@Value("${eu.datamanagementunit.transit.fhir.server.bearer.token:#{null}}")
	private String fhirStoreBearerToken;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "The timeout in milliseconds until a connection is established between the client and the DMS FHIR server", recommendation = "Change default value only if timeout exceptions occur")
	@Value("${eu.datamanagementunit.transit.fhir.server.timeout.connect:20000}")
	private int fhirStoreConnectTimeout;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "The timeout in milliseconds used when requesting a connection from the connection manager between the client and the DMS FHIR server", recommendation = "Change default value only if timeout exceptions occur")
	@Value("${eu.datamanagementunit.transit.fhir.server.timeout.connection.request:20000}")
	private int fhirStoreConnectionRequestTimeout;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Maximum period of inactivity in milliseconds between two consecutive data packets of the client and the DMS FHIR server", recommendation = "Change default value only if timeout exceptions occur")
	@Value("${eu.datamanagementunit.transit.fhir.server.timeout.socket:60000}")
	private int fhirStoreSocketTimeout;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "The client will log additional debug output", recommendation = "Change default value only if exceptions occur")
	@Value("${eu.datamanagementunit.transit.fhir.server.client.verbose:false}")
	private boolean fhirStoreHapiClientVerbose;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Proxy location, set if the server containing the FHIR data can only be reached through a proxy, uses value from DEV_DSF_PROXY_URL if not set", example = "http://proxy.foo:8080")
	@Value("${eu.datamanagementunit.transit.fhir.server.proxy.url:#{null}}")
	private String fhirStoreProxyUrl;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Proxy username, set if the server containing the FHIR data can only be reached through a proxy which requests authentication, uses value from DEV_DSF_PROXY_USERNAME if not set")
	@Value("${eu.datamanagementunit.transit.fhir.server.proxy.username:#{null}}")
	private String fhirStoreProxyUsername;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Proxy password, set if the server containing the FHIR data can only be reached through a proxy which requests authentication, uses value from DEV_DSF_PROXY_PASSWORD if not set", recommendation = "Use docker secret file to configure by using *${env_variable}_FILE*")
	@Value("${eu.datamanagementunit.transit.fhir.server.proxy.password:#{null}}")
	private String fhirStoreProxyPassword;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "The url of the oidc provider to request access tokens (token endpoint)", example = "http://foo.baz/realms/fhir-realm/protocol/openid-connect/token")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.issuer.url:#{null}}")
	private String fhirStoreOAuth2IssuerUrl;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Identifier of the client (username) used for authentication when accessing the oidc provider token endpoint")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.client.id:#{null}}")
	private String fhirStoreOAuth2ClientId;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Secret of the client (password) used for authentication when accessing the oidc provider token endpoint", recommendation = "Use docker secret file to configure by using *${env_variable}_FILE*")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.client.password:#{null}}")
	private String fhirStoreOAuth2ClientSecret;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "The timeout in milliseconds until a connection is established between the client and the oidc provider", recommendation = "Change default value only if timeout exceptions occur")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.timeout.connect:20000}")
	private int fhirStoreOAuth2ConnectTimeout;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Maximum period of inactivity in milliseconds between two consecutive data packets of the client and the oidc provider", recommendation = "Change default value only if timeout exceptions occur")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.timeout.socket:60000}")
	private int fhirStoreOAuth2SocketTimeout;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "PEM encoded file with one or more trusted root certificate to validate the oidc provider server certificate when connecting via https", recommendation = "Use docker secret file to configure", example = "/run/secrets/hospital_ca.pem")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.trust.certificates:#{null}}")
	private String fhirStoreOAuth2TrustStore;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Proxy location, set if the oidc provider can only be reached through a proxy, uses value from DEV_DSF_PROXY_URL if not set", example = "http://proxy.foo:8080")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.proxy.url:#{null}}")
	private String fhirStoreOAuth2ProxyUrl;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Proxy username, set if the oidc provider can only be reached through a proxy which requests authentication, uses value from DEV_DSF_PROXY_USERNAME if not set")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.proxy.username:#{null}}")
	private String fhirStoreOAuth2ProxyUsername;

	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "Proxy password, set if the oidc provider can only be reached through a proxy which requests authentication, uses value from DEV_DSF_PROXY_PASSWORD if not set", recommendation = "Use docker secret file to configure by using *${env_variable}_FILE*")
	@Value("${eu.datamanagementunit.transit.fhir.server.oauth2.proxy.password:#{null}}")
	private String fhirStoreOAuth2ProxyPassword;


	@ProcessDocumentation(processNames = {
			"datamanagementuniteu_storeControllerCreate" }, description = "To enable debug logging of FHIR resources set to `true`")
	@Value("${eu.datamanagementunit.transit.fhir.dataLoggingEnabled:false}")
	private boolean fhirDataLoggingEnabled;

	@Value("${dev.dsf.bpe.fhir.server.organization.identifier.value}")
	private String localIdentifierValue;

	public FhirClientFactory fhirClientFactory()
	{
		Path trustStorePath = checkExists(fhirStoreTrustStore);
		Path certificatePath = checkExists(fhirStoreCertificate);
		Path privateKeyPath = checkExists(fhirStorePrivateKey);

		String proxyUrl = fhirStoreProxyUrl, proxyUsername = fhirStoreProxyUsername,
				proxyPassword = fhirStoreProxyPassword;
		if (proxyUrl == null && api.getProxyConfig().isEnabled()
				&& !api.getProxyConfig().isNoProxyUrl(fhirStoreBaseUrl))
		{
			proxyUrl = api.getProxyConfig().getUrl();
			proxyUsername = api.getProxyConfig().getUsername();
			proxyPassword = api.getProxyConfig().getPassword() == null ? null
					: new String(api.getProxyConfig().getPassword());
		}

		return new FhirClientFactory(trustStorePath, certificatePath, privateKeyPath, fhirStorePrivateKeyPassword,
				fhirStoreConnectTimeout, fhirStoreSocketTimeout, fhirStoreConnectionRequestTimeout, fhirStoreBaseUrl,
				fhirStoreUsername, fhirStorePassword, fhirStoreBearerToken, tokenProvider(), proxyUrl, proxyUsername,
				proxyPassword, fhirStoreHapiClientVerbose, fhirContext, localIdentifierValue, dataLogger());
	}

	public TokenProvider tokenProvider()
	{
		return new OAuth2TokenProvider(tokenClient());
	}

	public TokenClient tokenClient()
	{
		Path trustStoreOAuth2Path = checkExists(fhirStoreOAuth2TrustStore);

		String proxyUrl = fhirStoreOAuth2ProxyUrl, proxyUsername = fhirStoreOAuth2ProxyUsername,
				proxyPassword = fhirStoreOAuth2ProxyPassword;
		if (proxyUrl == null && api.getProxyConfig().isEnabled()
				&& !api.getProxyConfig().isNoProxyUrl(fhirStoreOAuth2IssuerUrl))
		{
			proxyUrl = api.getProxyConfig().getUrl();
			proxyUsername = api.getProxyConfig().getUsername();
			proxyPassword = api.getProxyConfig().getPassword() == null ? null
					: new String(api.getProxyConfig().getPassword());
		}

		return new OAuth2TokenClient(fhirStoreOAuth2IssuerUrl, fhirStoreOAuth2ClientId, fhirStoreOAuth2ClientSecret,
				fhirStoreOAuth2ConnectTimeout, fhirStoreOAuth2SocketTimeout, trustStoreOAuth2Path, proxyUrl,
				proxyUsername, proxyPassword);
	}

	public DataLogger dataLogger()
	{
		return new DataLogger(fhirDataLoggingEnabled, fhirContext);
	}

	private Path checkExists(String file)
	{
		if (file == null)
			return null;
		else
		{
			Path path = Paths.get(file);

			if (!Files.isReadable(path))
				throw new RuntimeException(path + " not readable");

			return path;
		}
	}
	public String getFhirStoreBaseUrl()
	{
		return fhirStoreBaseUrl;
	}

	public void setFhirStoreBaseUrl(String fhirStoreBaseUrl)
	{
		this.fhirStoreBaseUrl = fhirStoreBaseUrl;
	}
}
