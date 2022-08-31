package ru.kata.bootstrap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.bootstrap.model.Role;

public interface RoleDao extends JpaRepository<Role, Long> {

    Role findRoleByRole(String role);
}