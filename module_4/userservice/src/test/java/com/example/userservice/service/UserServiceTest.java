package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository; // теперь мок репозитория

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto dto;

    @BeforeEach
    void setUp() {
        user = new User("Michael", "test@example.com", 33);
        user.setId(1L);

        dto = new UserDto();
        dto.setId(1L);
        dto.setName("Michael");
        dto.setEmail("test@example.com");
        dto.setAge(33);
    }

    @Test
    void testFindById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Michael", result.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_notFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.findById(2L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testCreateUser_success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto created = userService.create(dto);

        assertNotNull(created);
        assertEquals("Michael", created.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        dto.setName("Updated");
        Optional<UserDto> updated = userService.update(1L, dto);

        assertTrue(updated.isPresent());
        assertEquals("Updated", updated.get().getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean result = userService.deleteById(1L);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_notFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        boolean result = userService.deleteById(99L);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindAll_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals("Michael", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_empty() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDto> users = userService.findAll();

        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
}
