package com.example.userservice.dao;

import com.example.userservice.model.User;
import com.example.userservice.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplIT {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private static UserDaoImpl userDao;

    @BeforeAll
    static void setUp() {
        System.setProperty("db.url", postgres.getJdbcUrl());
        System.setProperty("db.username", postgres.getUsername());
        System.setProperty("db.password", postgres.getPassword());

        SessionFactory sf = HibernateUtil.getSessionFactory();
        assertNotNull(sf, "SessionFactory не инициализировался");
        userDao = new UserDaoImpl();
    }

    @BeforeEach
    void cleanAndPrepare() {
        var session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
        User u1 = new User("Michael", "michael@example.com", 20);
        User u2 = new User("Ivan", "ivan@example.com", 30);

        session.save(u1);
        session.save(u2);

        session.getTransaction().commit();
        session.close();
    }

    @Test
    void testCreateUser_success() {
        User user = new User("Test User", "test@example.com", 25);
        User saved = userDao.create(user);
        assertNotNull(saved.getId());
    }

    @Test
    void testFindById_success() {
        var users = userDao.findAll();
        Long id = users.get(0).getId();
        Optional<User> found = userDao.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Michael", found.get().getName());
    }

    @Test
    void testFindById_notFound() {
        assertTrue(userDao.findById(999L).isEmpty());
    }

    @Test
    void testFindAll_returnsTwoUsers() {
        var users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser_success() {
        var users = userDao.findAll();
        Long id = users.get(0).getId();
        Optional<User> found = userDao.findById(id);
        assertTrue(found.isPresent());
        User u = found.get();
        u.setName("Updated Name");
        User updated = userDao.update(u);
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDeleteUser_success() {
        var users = userDao.findAll();
        Long id = users.get(1).getId();
        boolean deleted = userDao.deleteById(id);
        assertTrue(deleted);
        assertTrue(userDao.findById(id).isEmpty());
    }

    @Test
    void testDeleteUser_notFound() {
        assertFalse(userDao.deleteById(123L));
    }
}
