package com.example.dits.service;

import com.example.dits.entity.User;
import com.example.dits.exceptions.CanNotAddUserException;

import java.util.List;

public interface UserService {
    void create(User user);
    void update(User user, int id);
    void save(User user) throws CanNotAddUserException;
    User getUserByLogin(String login);
    List<User> getAllUsers();
    void removeUser(int userId);
    User getUserById(int userId);
}
