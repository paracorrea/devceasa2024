package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.flc.springthymeleaf.domain.Authority;
import com.flc.springthymeleaf.domain.User;
import com.flc.springthymeleaf.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

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
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<String> roles) {
        Set<Authority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new Authority(role, user));
        }
        user.setAuthorities(authorities);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{username}")
    public String showEditUserForm(@PathVariable String username, Model model) {
        User user = userService.findUserByUsername(username);
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
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/edit/removeRole")
    public String removeRole(@RequestParam("username") String username, @RequestParam("roleToRemove") String roleToRemove) {
        User user = userService.findUserByUsername(username);
        Set<Authority> authorities = user.getAuthorities();
        authorities.removeIf(authority -> authority.getAuthority().equals(roleToRemove));
        user.setAuthorities(authorities);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/edit/toggleStatus")
    public String toggleUserStatus(@RequestParam("username") String username) {
        User user = userService.findUserByUsername(username);
        user.setEnabled(user.getEnabled() == 1 ? 0L : 1L);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return "redirect:/admin/users";
    }
}
