package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Michael", "test@example.com", 33);
        user.setId(1L);
    }

    @Test
    void testFindById_success() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Michael", result.get().getName());
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void testFindById_notFound() {
        when(userDao.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(2L);

        assertFalse(result.isPresent());
        verify(userDao, times(1)).findById(2L);
    }

    @Test
    void testCreateUser_success() {
        when(userDao.create(any(User.class))).thenReturn(user);

        User created = userService.create(new User("Ivan", "ivan@example.com", 30));

        assertNotNull(created);
        assertEquals("Michael", created.getName());
        verify(userDao, times(1)).create(any(User.class));
    }

    @Test
    void testUpdateUser_success() {
        user.setName("Updated");
        when(userDao.update(user)).thenReturn(user);

        User updated = userService.update(user);

        assertEquals("Updated", updated.getName());
        verify(userDao, times(1)).update(user);
    }

    @Test
    void testDeleteUser_success() {
        when(userDao.deleteById(1L)).thenReturn(true);

        boolean result = userService.deleteById(1L);

        assertTrue(result);
        verify(userDao, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_notFound() {
        when(userDao.deleteById(99L)).thenReturn(false);

        boolean result = userService.deleteById(99L);

        assertFalse(result);
        verify(userDao, times(1)).deleteById(99L);
    }
}
