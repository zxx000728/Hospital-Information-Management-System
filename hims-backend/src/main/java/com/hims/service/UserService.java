package com.hims.service;

import com.hims.domain.User;

import java.util.List;

public interface UserService {
    public void save(User user);

    public void delete(String id);

    public User find(String id);

    public List<User> findAll();
}
