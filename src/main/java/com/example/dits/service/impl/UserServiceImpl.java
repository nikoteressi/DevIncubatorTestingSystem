package com.example.dits.service.impl;

import com.example.dits.DAO.UserRepository;
import com.example.dits.entity.User;
import com.example.dits.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Transactional
    @Override
    public void update(User user, int id) {
        if (!user.getPassword().equals("")) user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userFromDb = repository.findById(id).orElse(null);
        assert userFromDb != null;
        if (user.getFirstName().equals(userFromDb.getFirstName())) userFromDb.setFirstName(user.getFirstName());
        if (user.getLastName().equals(userFromDb.getLastName())) userFromDb.setLastName(user.getLastName());
        if (user.getRole().getRoleName().equals(userFromDb.getRole().getRoleName())) userFromDb.setRole(user.getRole());
        if (user.getLogin().equals(userFromDb.getLogin())) userFromDb.setLogin(user.getLogin());
        userFromDb.setPassword(!user.getPassword().equals("") ? passwordEncoder.encode(user.getPassword()) : userFromDb.getPassword());
        repository.save(userFromDb);
    }

    @Transactional
    @Override
    public void save(User user) {

        repository.save(user);
    }

    @Transactional
    public User getUserByLogin(String login) {
        return repository.getUserByLogin(login);
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public void removeUser(int userId) {
        repository.deleteById(userId);
    }

    @Transactional
    @Override
    public User getUserById(int userId) {
        return repository.findById(userId).orElse(null);
    }

}
