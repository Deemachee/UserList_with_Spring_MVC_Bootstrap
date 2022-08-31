package ru.kata.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kata.bootstrap.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.bootstrap.dao.RoleDao;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> getAllRoles() {
        return new HashSet<>(roleDao.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByID(Long id) {
        return roleDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByRole(String role) {
        return roleDao.findRoleByRole(role);
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        if (role.getRole().equals("ROLE_ADMIN") || role.getRole().equals("ROLE_USER")) {
            if (getAllRoles().stream().noneMatch(r -> r.getRole().equals(role.getRole()))) {
                roleDao.save(role);
                log.info("Role <<{}>> was added to database", role.getRole());
            } else {
                log.info("Role <<{}>> was already exists", role.getRole());
            }
        } else {
            log.info("Can't add role <<{}>>, it can be only ROLE_ADMIN or ROLE_USER", role.getRole());
        }

    }
}
