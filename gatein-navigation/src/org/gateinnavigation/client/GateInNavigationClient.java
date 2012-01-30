package org.gateinnavigation.client;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.gateinnavigation.client.NavigationNode.NavigationNodeList;
import org.gateinnavigation.client.cache.NavigationNodeCache;
import org.gateinnavigation.http.RequestFactory;
import org.gateinnavigation.model.Child;
import org.gateinnavigation.model.Resource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;

//make domain classes for Navigation that will handle the API purely in Java
/**
 * @author William Antônio
 * 
 */
public class GateInNavigationClient {
	/**
	 * The username to access the API
	 */
	private String username;
	/**
	 * The password
	 */
	private String password;
	/**
	 * The base URL to perform requests
	 */
	private String baseUrl;

	/**
	 * The site you are trying to access, for example, classic
	 */
	private String site;

	private ClientRequestFactory requestFactory;

	private final String apiPrefix = "/rest/private/managed-components/mop/portalsites";

	private final String operation = "navigation";

	private GateInNavigationClient(String username, String password,
			String host, String site) {
		super();
		this.username = username;
		this.password = password;
		// The BaseURL for navigation is host + apiPrefix + site + navigation
		this.baseUrl = UriBuilder.fromPath(host).path(apiPrefix).path(site)
				.path(operation).build().toString();
		this.site = site;
		try {
			requestFactory = RequestFactory.createAuthenticatedFactory(
					username, password);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problems with URI syntax");
		}
	}

	/**
	 * It will create a new client
	 * 
	 * @param username
	 * @param password
	 * @param host
	 * @param site
	 * @return
	 */
	public static GateInNavigationClient create(String username,
			String password, String host, String site) {
		return new GateInNavigationClient(username, password, host, site);
	}

	/**
	 * Get the root navigation node
	 * 
	 * @return
	 */
	public NavigationNode getRootNavigationNode(boolean recursive) {
		NavigationNode root = getNavigationNode("", recursive);
		root.setParent(null);
		root.setName(site);
		root.setPath("/");
		return root;
	}

	/**
	 * Get a navigation node from a specific URL
	 * 
	 * @param path
	 * @return
	 */
	public NavigationNode getNavigationNode(String path, boolean recursive) {
		String url = UriBuilder.fromPath(baseUrl).path(path).build().toString();
		NavigationNode node = NavigationNodeCache.getNode(url);
		if (node != null)
			return node;

		// the difference from this method to getRootNavigationNode is that we
		// will try to create a parent to be searched in future (if required)
		Resource r = retrieveResource(url);
		node = buildNavigationNodeFromResource(r, path, recursive);
		NavigationNodeCache.cacheNode(node);
		node.setSelf(url);
		return node;
	}

	/**
	 * Get the root navigation node from a specific group
	 * 
	 * @param group
	 * @return
	 */
	public NavigationNode getRootNavigationNode(String group) {
		throw new Error("Working in progress...");
	}

	/**
	 * It will create our object from a resource from API
	 * 
	 * @param resource
	 * @return
	 */
	private NavigationNode buildNavigationNodeFromResource(Resource resource,
			String path, boolean recursive) {
		NavigationNode node = new NavigationNode(this);
		NavigationNodeList navigationNodeList = new NavigationNodeList();
		node.setDescription(resource.getDescription());
		node.setPath(path);

		if (resource.getChildren() != null) {
			Set<Child> children = resource.getChildren();
			for (Child child : children) {
				NavigationNode n = new NavigationNode(this);
				n.setName(child.getName());
				n.setDescription(child.getDescription());
				n.setParent(node);
				n.setPath(UriBuilder.fromPath("").path(node.getPath())
						.path(child.getName()).build().toString());
				navigationNodeList.add(n);

				if (recursive) {
					getNavigationNode(n.getPath(), recursive);
				}
			}
		}
		node.setChildren(navigationNodeList);
		int index = node.getPath().lastIndexOf('/');
		if (index != -1)
			node.setName(node.getPath().substring(index + 1));
		else
			node.setName(path);
		return node;
	}

	@SuppressWarnings("unchecked")
	public InputStream getResourceZip(String path) {
		System.out.println("Downloading zip");
		String url = UriBuilder.fromPath(baseUrl).path(path + ".zip").build()
				.toString();
		ClientRequest cr = requestFactory.createRequest(url);
		try {
			return (InputStream) cr.get().getEntity(InputStream.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}		
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getBaseURL() {
		return baseUrl;
	}

	public String getSite() {
		return site;
	}

	@SuppressWarnings("unchecked")
	private Resource retrieveResource(String url) {
		ClientRequest cr = requestFactory.createRequest(url);
		cr.accept(MediaType.APPLICATION_XML);
		try {
			return (Resource) cr.get().getEntity(Resource.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error retrieving resource at " + url);
		}
	}

}
