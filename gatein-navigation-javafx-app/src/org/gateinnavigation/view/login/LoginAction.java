package org.gateinnavigation.view.login;

import org.gateinnavigation.client.NavigationNode;

public interface LoginAction {
	public void onSucess(NavigationNode node, LoginData loginData);

	public void onFail(String message);
}
