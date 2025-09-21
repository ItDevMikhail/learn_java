package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> findById(Long id) { return userDao.findById(id); }
    public List<User> findAll() { return userDao.findAll(); }
    public User create(User user) { return userDao.create(user); }
    public User update(User user) { return userDao.update(user); }
    public boolean deleteById(Long id) { return userDao.deleteById(id); }
}
