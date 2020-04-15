package org.cruzl.spring.database.auth.controllers.urls;

import org.springframework.stereotype.Component;

@Component
public class UserUrl {

	public static final String USER_LOGGED = "/user";
	public static final String USER_LOGGED_EDIT = "/user/edit";
	public static final String USER = "/user/{username}";
	public static final String USER_EDIT = "/user/{username}/edit";
	public static final String LOGIN = "/login";
	public static final String LOGOUT = "/logout";

	public String getLogoutUrl() {
		return LOGOUT;
	}

	public String getUserLoggedUrl() {
		return USER_LOGGED;
	}

	public String getUserLoggedEdit() {
		return USER_LOGGED_EDIT;
	}

}
