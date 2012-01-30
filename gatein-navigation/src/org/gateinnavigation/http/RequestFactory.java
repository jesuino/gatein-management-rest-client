package org.gateinnavigation.http;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.gateinnavigation.client.LoggingInterceptor;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;

@SuppressWarnings("deprecation")
public class RequestFactory {
	public static ClientRequestFactory createAuthenticatedFactory(
			String username, String password) throws URISyntaxException {
		// Setting Credentials though an HttpExecutor
		Credentials credentials = new UsernamePasswordCredentials(username,
				password);
		HttpClient httpClient = new HttpClient();
		httpClient.getState().setCredentials(AuthScope.ANY, credentials);
		httpClient.getParams().setAuthenticationPreemptive(true);
		ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);
		ClientRequestFactory clientRequestFactory = new ClientRequestFactory(
				clientExecutor, new URI(""));
		clientRequestFactory.getPrefixInterceptors().registerInterceptor(
				new LoggingInterceptor());
		return clientRequestFactory;
	}
}
