package ru.kata.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.bootstrap.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.bootstrap.dao.UserDao;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
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
    public void updateUser(User user) {
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