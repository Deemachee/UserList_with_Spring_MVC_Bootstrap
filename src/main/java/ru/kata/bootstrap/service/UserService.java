package ru.kata.bootstrap.service;

import ru.kata.bootstrap.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();

    void addUser(User user);

    void saveUser(User user, Long[] checked);

    void deleteById(Long id);

    User getUserById(Long id);

    void updateUser(User user, Long[] checked);

    User getUserByUsername(String username);
}