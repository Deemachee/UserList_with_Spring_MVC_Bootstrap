package ru.kata.bootstrap.service;

import ru.kata.bootstrap.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getAllRoles();

    Role getRoleByID(Long id);

    Role getRoleByRole(String role);

    void addRole(Role role);
}