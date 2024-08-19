package com.library.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.library.dto.UserDto;
import com.library.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "register";
	}
	
	
	@PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        if (userService.emailExists(userDto.getEmail())) {
            model.addAttribute("errorMessage", "Email is already exist!");
            return "register"; 
        }

        userService.save(userDto);
        model.addAttribute("success", true);
        model.addAttribute("delay", 5);
        return "register";
    }

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/admin-page")
	public String adminpage(Model model,Principal principal) {
		UserDetails userDetails=userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user",userDetails);
		return "admin";
	}
	
	@GetMapping("/user-page")
	public String userpage(Model model,Principal principal) {
		UserDetails userDetails=userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user",userDetails);
		return "user";
	}
}
