package com.blog.travel.service;

import com.blog.travel.entity.User;

import java.util.Optional;

public interface UserService {
    User add(User user);

    User update(User updateUser);

    Optional<User> get(long id);

    void delete(long id);
}
