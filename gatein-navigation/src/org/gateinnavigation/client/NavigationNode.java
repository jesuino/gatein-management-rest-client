package org.gateinnavigation.client;

import java.util.ArrayList;

/**
 * 
 * This class will allow the navigation throw the portal site with a simpler
 * interface
 * 
 * @author jesuino
 * 
 */
public class NavigationNode {
	/**
	 * The path to this node from the base URL
	 */
	private String path;
	private String description;
	private String name;
	private NavigationNode parent;
	private NavigationNodeList children;
	private String self;
	private String url;

	/**
	 * This is class responsible for search the children
	 */
	private GateInNavigationClient client;

	public NavigationNode() {

	}

	public NavigationNode(GateInNavigationClient client) {
		this.client = client;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NavigationNode getParent() {
		return parent;
	}

	public void setParent(NavigationNode parent) {
		this.parent = parent;
	}

	public NavigationNodeList getChildren() {
		return children;
	}

	public void setChildren(NavigationNodeList children) {
		this.children = children;
	}

	public GateInNavigationClient getClient() {
		return client;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static class NavigationNodeList extends ArrayList<NavigationNode> {

		private static final long serialVersionUID = 1L;

		/*
		 * Will load the entire NavigationNode when required. A central cache
		 * will improve the performance and avoid too much requests
		 * 
		 * @see java.util.ArrayList#get(int)
		 */
		@Override
		public NavigationNode get(int i) {
			NavigationNode node = super.get(i);
			return node.getClient().getNavigationNode(node.getPath(), true);
		}

	}
}
