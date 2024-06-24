package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.flc.springthymeleaf.domain.User;
import com.flc.springthymeleaf.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "registro/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
       
    	 if (user.getEnabled() == null) {
             user.setEnabled(0L);
         }
    	//userService.registerUser(user);
        return "redirect:/login"; 
    }

    @GetMapping("/change_password")
    public String showChangePasswordForm() {
        return "registro/change_password";
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam String username, @RequestParam String newPassword) {
      //  userService.changePassword(username, newPassword);
        return "redirect:/login";
    }
    
    
}
