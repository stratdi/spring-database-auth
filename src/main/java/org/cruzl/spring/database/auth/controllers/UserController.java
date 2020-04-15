package org.cruzl.spring.database.auth.controllers;

import java.security.Principal;
import java.util.Optional;

import org.cruzl.spring.database.auth.controllers.urls.UserUrl;
import org.cruzl.spring.database.auth.model.User;
import org.cruzl.spring.database.auth.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@GetMapping(UserUrl.USER_LOGGED_EDIT)
	public String edit(@NonNull final Principal principal, @ModelAttribute final User user,
			final BindingResult bindingResult, Model model) {
		return this.edit(principal.getName(), user, bindingResult, model);
	}

	@PostMapping(UserUrl.USER_LOGGED_EDIT)
	public String editProcess(@NonNull final Principal principal, @RequestParam MultipartFile image,
			@ModelAttribute final User user, final BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		return this.editProcess(principal.getName(), image, user, bindingResult, redirectAttributes);
	}

	@GetMapping(UserUrl.USER_EDIT)
	public String edit(@PathVariable final String username, @ModelAttribute User user,
			final BindingResult bindingResult, Model model) {
		if (!bindingResult.hasErrors()) {
			Optional<User> userDb = this.userService.get(username);
			if (userDb.isPresent()) {
				user = userDb.get();
				model.addAttribute("user", user);
			}
		}
		return "user/edit";
	}

	/* TODO: improve MultipartFile */
	@PostMapping(UserUrl.USER_EDIT)
	public String editProcess(@PathVariable final String username, @RequestParam MultipartFile image,
			@ModelAttribute User user, final BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		try {
			if (image.getOriginalFilename().isEmpty()) {
				user.setImage(this.userService.get(username).get().getImage());
			} else {
				user.setImage(image.getBytes());
			}
		} catch (Exception e) {
			log.error("", e);
		}

		this.userService.save(user);
		redirectAttributes.addFlashAttribute("notification", "ok");
		return "redirect:/user/details";

	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", true);
		return "login";
	}

}
