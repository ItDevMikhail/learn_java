package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.example.userservice.events.NotificationEvent;
import com.example.userservice.events.Operation;
import com.example.userservice.messaging.UserEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repo;
    private final UserEventPublisher events;

    public UserService(UserRepository repo, UserEventPublisher events) {
        this.repo = repo;
        this.events = events;
    }

    public UserDto create(UserDto dto) {
        User saved = repo.save(UserMapper.toEntity(dto));
        events.publish(new NotificationEvent(Operation.CREATE, saved.getEmail()));
        return UserMapper.toDto(saved);
    }

    public Optional<UserDto> findById(Long id) {
        return repo.findById(id).map(UserMapper::toDto);
    }

    public List<UserDto> findAll() {
        return repo.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public Optional<UserDto> update(Long id, UserDto dto) {
        return repo.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setEmail(dto.getEmail());
            existing.setAge(dto.getAge());
            return UserMapper.toDto(repo.save(existing));
        });
    }

    public boolean deleteById(Long id) {
        if (repo.existsById(id)) {
            String email = repo.findById(id).map(User::getEmail).orElse(null);
            repo.deleteById(id);
            if (email != null) {
                events.publish(new NotificationEvent(Operation.DELETE, email));
            }
            return true;
        }
        return false;
    }
}
