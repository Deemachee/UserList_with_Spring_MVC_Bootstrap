package ru.kata.bootstrap.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.bootstrap.model.User;
import ru.kata.bootstrap.service.RoleService;
import ru.kata.bootstrap.service.UserService;

import java.util.Arrays;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getUserList(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("userList", userService.getAllUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("roleList", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping(value = "/add")
    public String addUser(@ModelAttribute User newUser,
                          @RequestParam(value = "checked", required = true) Long[] checked) {
        userService.saveUser(newUser, checked);
        return "redirect:/admin";
    }

    @PatchMapping(value = "/edit/{id}")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(value = "checked", required = false) Long[] checked) {
        userService.updateUser(user, checked);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String getUserId(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}