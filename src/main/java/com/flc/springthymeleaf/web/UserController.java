package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.flc.springthymeleaf.domain.User;
import com.flc.springthymeleaf.service.UserService;

@Controller
public class UserController {

	 @Autowired
	    private UserService userService;

	    @GetMapping("/usuario/alterar-senha")
	    public String showChangePasswordForm(Model model) {
	        return "usuario/alterar-senha";
	    }

	    @PostMapping("/usuario/alterar-senha")
	    public String changePassword(@RequestParam("oldPassword") String oldPassword,
	                                 @RequestParam("newPassword") String newPassword,
	                                 Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	        User user = userService.findUserByUsername(username);

	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
	            model.addAttribute("fail", "Senha atual incorreta.");
	            return "usuario/alterar-senha";
	        }

	        user.setPassword(passwordEncoder.encode(newPassword));
	        userService.saveUser(user);
	        model.addAttribute("success", "Senha alterada com sucesso.");
	        return "usuario/alterar-senha";
	    }
	
    
}
