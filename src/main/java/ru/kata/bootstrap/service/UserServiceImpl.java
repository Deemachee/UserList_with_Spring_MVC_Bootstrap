package ru.kata.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.bootstrap.dao.RoleDao;
import ru.kata.bootstrap.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.bootstrap.dao.UserDao;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUser() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void addUser(User user) {
        User exist = getUserByUsername(user.getUsername());
        if (exist != null) {
            log.info("User <<{}>> is already exist", user.getUsername());
        } else {
            log.info("User <<{}>> was added to database", user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.save(user);
        }

    }

    @Override
    public void saveUser(User user, Long[] checked) {
        if (checked.length == 1 && roleService.getRoleByID(checked[0]).getRole().equals("ROLE_ADMIN")) {
            user.setRoles(Set.of(roleService.getRoleByRole("ROLE_ADMIN"), roleService.getRoleByRole("ROLE_USER")));
        } else {
            Arrays.stream(checked).forEach(count -> user.setOneRole(roleService.getRoleByID(count)));
        }
        addUser(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateUser(User user, Long[] checked) {
        if (user.getPassword() == null) {
            user.setPassword(getUserById(user.getId()).getPassword());
        }
        if (checked == null) {
            user.setRoles(getUserById(user.getId()).getRoles());
        } else if (checked.length == 1 && roleService.getRoleByID(checked[0]).getRole().equals("ROLE_ADMIN")) {
            user.setRoles(Set.of(roleService.getRoleByRole("ROLE_ADMIN"), roleService.getRoleByRole("ROLE_USER")));
        } else {
            Arrays.stream(checked).forEach(count -> user.setOneRole(roleService.getRoleByID(count)));
        }
        String passwordFromForm = user.getPassword();
        String encodedPasswordFromBase = getUserById(user.getId()).getPassword();
        if (getUserById(user.getId()).getPassword().equals(user.getPassword())) {
            user.setPassword(encodedPasswordFromBase);
        } else {
            if (passwordEncoder.matches(passwordFromForm, encodedPasswordFromBase)) {
                user.setPassword(encodedPasswordFromBase);
            } else {
                user.setPassword(passwordEncoder.encode(passwordFromForm));
            }
        }
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}