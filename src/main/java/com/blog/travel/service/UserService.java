package com.blog.travel.service;

import com.blog.travel.entity.User;
import com.blog.travel.exception.UserAlreadyExistException;

import java.util.Optional;

public interface UserService {
    User add(User user) throws UserAlreadyExistException;

    User update(User updateUser);

    Optional<User> get(long id);

    void delete(long id);

    boolean emailTaken(String email);
}
