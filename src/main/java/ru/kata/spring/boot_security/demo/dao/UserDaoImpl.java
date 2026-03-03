package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    // Создаём и внедряем сущности для работы с БД
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
//  --- Ищем юзера и создаём JPQL запрос:
//  --- distinct        - убираем дубликаты (один юзер, с 2 ролями 2-жды не появится)
//  --- left join fetch - подгружаем роли и исключаем появление LazyInitializationException
//  --- getResultList   - выполняем запрос и возвращаем список
        return entityManager.createQuery(
                "select distinct u from User u left join fetch u.roles", User.class
        ).getResultList();
    }

    @Override
    public User findById(Long id) {
        // Ищем юзера по ID
        return entityManager.find(User.class, id);
    }

    @Override
    public void save(User user) {
        // Сохраняем юзера как новый объект, в БД (работает только с новыми, транзиентными объектами)
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        // Обновляем юзера как объект, в БД (работает как с новыми, так и с сущ. объектами)
        entityManager.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        // Находим и удаляем юзера, которого нашли через find
        User user = entityManager.find(User.class, id);
        if (user != null) entityManager.remove(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
//  --- :username            - именованный параметр
//  --- setParameter         - подставляем значение
//  --- getResultList        - возвращаем список, хотя ожидаем одного пользователя
//  --- stream().findFirst() - берем первый элемент из списка, если есть, и заворачиваем в Optional
        return entityManager.createQuery(
                        "select distinct u from User u left join fetch u.roles where u.username = :username",
                        User.class
                )
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
    }
}