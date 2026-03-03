package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public void create(User user, List<Long> roleId) {
        user.setRoles(resolveRoles(roleId));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);
    }

    @Override
    public void update(User user, List<Long> roleId) {
        User old = userDao.findById(user.getId());
        if (old == null) {
            throw new IllegalArgumentException("User not found: id=" + user.getId());
        }

        user.setRoles(resolveRoles(roleId));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(old.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userDao.update(user);
    }

    @Override
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    private Set<Role> resolveRoles(List<Long> roleId) {
        if (roleId == null || roleId.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(roleService.findById(roleId));
    }
}