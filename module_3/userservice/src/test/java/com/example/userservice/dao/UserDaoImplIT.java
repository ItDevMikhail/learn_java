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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @Test
    @Order(1)
    void testCreateUser() {
        User user = new User("Test User", "test@example.com", 25);
        User saved = userDao.create(user);
        assertNotNull(saved.getId());
    }

    @Test
    @Order(2)
    void testFindById() {
        Optional<User> found = userDao.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getName());
    }

    @Test
    @Order(3)
    void testUpdateUser() {
        Optional<User> found = userDao.findById(1L);
        assertTrue(found.isPresent());
        User u = found.get();
        u.setName("Updated Name");
        User updated = userDao.update(u);
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    @Order(4)
    void testDeleteUser() {
        boolean deleted = userDao.deleteById(1L);
        assertTrue(deleted);
        assertTrue(userDao.findById(1L).isEmpty());
    }
}
