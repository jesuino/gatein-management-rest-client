package org.gateinnavigation.view.login;

/**
 * The data entered by the user in login form
 * 
 * @author William Antônio
 * 
 */
public class LoginData {
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
	private String host;

	/**
	 * The site you are trying to access, for example, classic
	 */
	private String site;
	
	/**
	 * The context of the portal
	 */
	private String portalContext;

	public LoginData() {
		super();
		// TODO Auto-generated constructor stub
	}


	public LoginData(String username, String password, String host,
			String site, String portalContext) {
		super();
		this.username = username;
		this.password = password;
		this.host = host;
		this.site = site;
		this.portalContext = portalContext;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPortalContext() {
		return portalContext;
	}

	public void setPortalContext(String portalContext) {
		this.portalContext = portalContext;
	}
	

}
