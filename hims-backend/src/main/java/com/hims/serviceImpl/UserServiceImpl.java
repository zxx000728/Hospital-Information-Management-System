package com.hims.serviceImpl;

import com.hims.domain.User;
import com.hims.exception.BadCredentialsException;
import com.hims.exception.UserNotFoundException;
import com.hims.repository.UserRepository;
import com.hims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> login(String id, String password) {
        User user = find(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        } else if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            return map;
        }
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(String id) {
        userRepository.delete(id);
    }

    @Override
    public User find(String id) {
        return userRepository.find(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
