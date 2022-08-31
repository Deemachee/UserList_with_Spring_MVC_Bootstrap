package ru.kata.bootstrap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.bootstrap.model.User;


@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
}