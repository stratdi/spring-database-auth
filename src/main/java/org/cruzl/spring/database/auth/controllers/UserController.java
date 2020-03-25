package org.cruzl.spring.database.auth.controllers;

import java.security.Principal;

import org.cruzl.spring.database.auth.controllers.urls.UserUrl;
import org.cruzl.spring.database.auth.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.NonNull;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(UserUrl.USER_LOGGED)
	public String details(@NonNull final Principal principal, @NonNull final Model model) {
		return this.details(principal.getName(), model);
	}

	@GetMapping(UserUrl.USER)
	public String details(@PathVariable final String username, @NonNull final Model model) {
		model.addAttribute("user", this.userService.get(username));
		return "user/details";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", true);
		return "login";
	}

}
