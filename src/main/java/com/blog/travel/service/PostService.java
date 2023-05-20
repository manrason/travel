package com.blog.travel.service;

import com.blog.travel.entity.Post;
import com.blog.travel.exception.PostAlreadyExistException;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(Post post) throws PostAlreadyExistException;
    List<Post> getAllPost();
    Optional<Post> getPost(long id);
    Post updatePost(Post updatedPost);
    void deletePost(long id);

}
