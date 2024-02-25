package com.blog.travel.service;

import com.blog.travel.entity.User;
import com.blog.travel.exception.UserAlreadyExistException;
import com.blog.travel.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User add(User user) throws UserAlreadyExistException {
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        if (userOptional.isPresent()){
            throw new UserAlreadyExistException("User already exist");
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User updateUser) {
        return userRepository.save(updateUser);
    }

    @Override
    public Optional<User> get(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
