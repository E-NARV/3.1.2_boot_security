package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    User findById(Long id);

    void create(User user, List<Long> roleId);
    void update(User user, List<Long> roleId);

    void delete(Long id);
    Optional<User> findByUsername(String username);
}