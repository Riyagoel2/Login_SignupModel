package com.drive.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.drive.model.User;
import com.drive.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@GetMapping(value = { "/", "/login" })
	public String login() {
		return "login";
	}

	@GetMapping(value = "/sign-up")
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "signup";

	}

	@PostMapping(value = "/sign-up")
	public String createnewUser(@Valid User user, BindingResult bindingResult, Model model) {
		User userExists = userService.findUserByUserName(user.getUsername());
		if (userExists != null) {
			bindingResult.rejectValue("username", "error.user",
					"There is already a user registered with the user name provided");
		} else {

			userService.saveUser(user);
			model.addAttribute("user", new User());
			model.addAttribute("successMessage", "User has been registered successfully");
		}
		return "signup";

	}

	@GetMapping(value = "dummy")  
	public String home(Model model)

	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
	        model.addAttribute("username", "Welcome " + user.getUsername() + " !");
		return "file/home";
	}

}
