package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void testGetAllUsers() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Michael");
        dto.setEmail("test@example.com");
        dto.setAge(33);

        when(service.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Michael"));
    }

    @Test
    void testGetUserById_success() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Michael");
        dto.setEmail("test@example.com");
        dto.setAge(33);

        when(service.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Michael"));
    }

    @Test
    void testGetUserById_notFound() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("New User");
        dto.setEmail("new@example.com");
        dto.setAge(25);

        when(service.create(any(UserDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content("{\"name\":\"New User\",\"email\":\"new@example.com\",\"age\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void testUpdateUser_success() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Updated");
        dto.setEmail("updated@example.com");
        dto.setAge(40);

        when(service.update(eq(1L), any(UserDto.class))).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/users/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Updated\",\"email\":\"updated@example.com\",\"age\":40}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testUpdateUser_notFound() throws Exception {
        when(service.update(eq(99L), any(UserDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/99")
                        .contentType("application/json")
                        .content("{\"name\":\"Ghost\",\"email\":\"ghost@example.com\",\"age\":50}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_success() throws Exception {
        when(service.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_notFound() throws Exception {
        when(service.deleteById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound());
    }
}
