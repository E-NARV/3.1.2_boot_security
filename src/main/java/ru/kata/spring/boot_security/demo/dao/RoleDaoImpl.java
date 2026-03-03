package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery(
                "select r from Role r", Role.class).getResultList();
    }

    @Override
    public List<Role> findById(Iterable<Long> id) {
        return entityManager.createQuery(
                "select r from Role r where r.id in :id", Role.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Optional<Role> findByName(String name) {
        List<Role> roles = entityManager.createQuery(
                "select r from Role r where r.name = :name", Role.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();
        return roles.stream().findFirst();
    }

    @Override
    public Role save(Role role) {
        entityManager.persist(role);
        return role;
    }
}