package org.gateinmanager.test.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.gateinnavigation.model.Child;
import org.gateinnavigation.model.Link;
import org.gateinnavigation.model.Operation;
import org.gateinnavigation.model.Resource;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;

/**
 * 
 * It will just test the request is being parsed to domain objects...
 * 
 * @author jesuino
 * 
 */
@SuppressWarnings("deprecation")
public class TestModel {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "gtn";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		ClientRequestFactory factory = createFactory(USERNAME, PASSWORD);
		Scanner sc = new Scanner(System.in);
		String in;

		Resource resource = null;
		System.out.println("\t\t*****GateIn Management API*****\n\n");

		System.out
				.println(">> Enter the HOST to start navigating or press"
						+ " Q to quit (the base URL is "
						+ "http://192.168.56.101:8080/rest/private/managed-components)\n");
		while (!(in = sc.next()).startsWith("Q")) {
			// Entered a URL, just load Resource and print it
			if (in.startsWith("http://")) {
				ClientRequest cr = factory.createRequest(in);
				cr.accept(MediaType.APPLICATION_XML);
				try {
					System.out.println("Loading...");
					resource = (Resource) cr.get().getEntity(Resource.class);
					System.out.println("Resource loaded!");
				} catch (Exception e) {
					System.out
							.println("\n\n>>>  Something went wrong when loading your URL...\n");
					e.printStackTrace();
				}
			} else {
				System.out
						.println("\nPlease enter a valid URL or press Q to quit...");
			}
			if (resource != null)
				printResource(resource);
			System.out.println("\n>> Enter URL or Q to quit: ");
		}

	}

	public static ClientRequestFactory createFactory(String username,
			String password) throws URISyntaxException {
		// Setting Credentials though an HttpExecutor
		Credentials credentials = new UsernamePasswordCredentials(username,
				password);
		HttpClient httpClient = new HttpClient();
		httpClient.getState().setCredentials(AuthScope.ANY, credentials);
		httpClient.getParams().setAuthenticationPreemptive(true);
		ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

		// creating the factory
		return new ClientRequestFactory(clientExecutor, new URI(""));
	}

	public static void printResource(Resource resource) {
		System.out.println("**\t" + resource.getDescription() + "\t**\n");
		System.out.println("Possible Operations:");
		List<Operation> operations = resource.getOperations();
		if (operations != null) {
			for (Operation operation : operations) {
				Link link = operation.getOperationLink();
				System.out.println("\t* " + operation.getOperationName() + ": "
						+ operation.getOperationDescription() + "\n\t   rel: "
						+ link.getRel() + ". Located at " + link.getHref());
			}
		}

		Set<Child> children = resource.getChildren();
		if (children != null) {
			System.out.println("\nChildren:");
			for (Child child : children) {
				Link link = child.getLink();
				System.out.println("\t* " + child.getName() + ": "
						+ child.getDescription() + "\n\t   rel: "
						+ link.getRel() + ". Located at " + link.getHref());
			}
		}

	}
}