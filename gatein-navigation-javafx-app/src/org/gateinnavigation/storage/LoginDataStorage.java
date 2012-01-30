package org.gateinnavigation.storage;

import org.gateinnavigation.view.login.LoginData;

public class LoginDataStorage {

	private static LoginData loginData;

	static {
		loginData = new LoginData("root", "", "http://localhost:8080",
				"classic", "portal");
	}

	public static void save(LoginData loginData) {
		// TODO storage this data
	}

	public static LoginData retrieve() {
		return loginData;
	}
}
