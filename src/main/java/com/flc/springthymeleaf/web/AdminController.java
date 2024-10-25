package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.flc.springthymeleaf.domain.Authority;
import com.flc.springthymeleaf.domain.User;
import com.flc.springthymeleaf.service.UserService;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());
   
    
    @GetMapping("/admin/users")
    public String showUserManagement(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/user-management";
    }

    @GetMapping("/admin/users/new")
    public String showNewUserForm(Model model) {
        User newUser = new User();
        model.addAttribute("user", newUser);
        return "admin/user-form";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<String> roles, @RequestParam("password") String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        Set<Authority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new Authority(role, user));
        }
        user.setAuthorities(authorities);
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        LOGGER.info("Usuário Salvo: " + user.getUsername());
        LOGGER.info("Criado por: " + authentication.getName());
        
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{username}")
    public String showEditUserForm(@PathVariable String username, Model model) {
        
    	User user = userService.findUserByUsername(username);
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Usuário Editado: " + user.getUsername());
        LOGGER.info("Editado por: " + authentication.getName());
      
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    @PostMapping("/admin/users/update")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<String> roles) {
        User existingUser = userService.findUserByUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEnabled(user.getEnabled());

        Set<Authority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new Authority(role, existingUser));
        }
        existingUser.setAuthorities(authorities);
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Usuário Editado e salvo: " + user.getUsername());
        LOGGER.info("Editado e salvo por: " + authentication.getName());
       
        userService.saveUser(existingUser);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/edit/addRole")
    public String addRole(@RequestParam("username") String username, @RequestParam("newRole") String newRole) {
        User user = userService.findUserByUsername(username);
        Set<Authority> authorities = user.getAuthorities();
        if (authorities.stream().noneMatch(auth -> auth.getAuthority().equals(newRole))) {
            authorities.add(new Authority(newRole, user));
        }
        user.setAuthorities(authorities);
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Usuário adicionada regra: " + user.getUsername());
        LOGGER.info("Alterado por: " + authentication.getName());
        
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/edit/removeRole")
    public String removeRole(@RequestParam("username") String username, @RequestParam("roleToRemove") String roleToRemove) {
        User user = userService.findUserByUsername(username);
        Set<Authority> authorities = user.getAuthorities();
        authorities.removeIf(authority -> authority.getAuthority().equals(roleToRemove));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Usuário removida regra: " + user.getUsername());
        LOGGER.info(" Removido por " + authentication.getName());
        
        user.setAuthorities(authorities);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/edit/toggleStatus")
    public String toggleUserStatus(@RequestParam("username") String username) {
        User user = userService.findUserByUsername(username);
        user.setEnabled(user.getEnabled() == 1 ? 0L : 1L);
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Usuário alterado sua regra: " + user.getUsername());
        LOGGER.info("Alterada regra por " + authentication.getName());
        
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/changePassword")
    public String changeUserPassword(@RequestParam("username") String username,
                                     @RequestParam("newPassword") String newPassword) {
        User user = userService.findUserByUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Usuário alterado sua senha: " + user.getUsername());
        LOGGER.info("Alterada senha por " + authentication.getName());
        
        userService.saveUser(user);
        return "redirect:/admin/users";
    }
}
