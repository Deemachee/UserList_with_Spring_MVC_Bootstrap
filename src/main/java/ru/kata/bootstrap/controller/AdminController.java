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
        if (checked.length == 1 && roleService.getRoleByID(checked[0]).getRole().equals("ROLE_ADMIN")) {
            newUser.setRoles(Set.of(roleService.getRoleByRole("ROLE_ADMIN"), roleService.getRoleByRole("ROLE_USER")));
        } else {
            Arrays.stream(checked).forEach(count -> newUser.setOneRole(roleService.getRoleByID(count)));
        }
        userService.addUser(newUser);
        return "redirect:/admin";
    }

    @PatchMapping(value = "/edit/{id}")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(value = "checked", required = false) Long[] checked) {
        if (user.getPassword() == null) {
            user.setPassword(userService.getUserById(user.getId()).getPassword());
        }
        if (checked == null) {
            user.setRoles(userService.getUserById(user.getId()).getRoles());
        } else if (checked.length == 1 && roleService.getRoleByID(checked[0]).getRole().equals("ROLE_ADMIN")) {
            user.setRoles(Set.of(roleService.getRoleByRole("ROLE_ADMIN"), roleService.getRoleByRole("ROLE_USER")));
        } else {
            Arrays.stream(checked).forEach(count -> user.setOneRole(roleService.getRoleByID(count)));
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String getUserId(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}