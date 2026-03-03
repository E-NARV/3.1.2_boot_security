package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao {
    List<Role> findAll();
    List<Role> findById(Iterable<Long> id);
    Optional<Role> findByName(String name);
    Role save(Role role);
}