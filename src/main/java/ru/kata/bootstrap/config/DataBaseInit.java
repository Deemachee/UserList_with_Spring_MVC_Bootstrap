package ru.kata.bootstrap.config;

import org.springframework.stereotype.Component;
import ru.kata.bootstrap.model.Role;
import ru.kata.bootstrap.model.User;
import ru.kata.bootstrap.service.RoleService;
import ru.kata.bootstrap.service.UserService;
import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class DataBaseInit {
    private final UserService userService;
    private final RoleService roleService;

    public DataBaseInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void startDB() {
        User user = new User("user@gmail.com", "user", "user", 20, "user");
        User admin = new User("admin@gmail.com", "admin", "admin", 30, "admin");
        Role userRole = new Role("ROLE_USER");
        Role adminRole = new Role("ROLE_ADMIN");
        roleService.addRole(userRole);
        roleService.addRole(adminRole);
        user.setRoles(Set.of(userRole));
        admin.setRoles(Set.of(adminRole, userRole));
        userService.addUser(user);
        userService.addUser(admin);
    }
}