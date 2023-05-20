package com.blog.travel.controller;

import com.blog.travel.entity.Post;
import com.blog.travel.exception.PostAlreadyExistException;
import com.blog.travel.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostServiceImpl postServiceImpl;

    @GetMapping("/get")
    public ResponseEntity<List<Post>> getAllPosts() {
        try {
            postServiceImpl.getAllPost();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") long id) {
        Optional<Post> post = postServiceImpl.getPost(id);
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Post> save(@Validated @RequestParam Post post) {
        try {
            return new ResponseEntity<>(postServiceImpl.save(post), HttpStatus.CREATED);
        } catch (PostAlreadyExistException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Post> update(@Validated @RequestParam Post post) {
        return new ResponseEntity<>(postServiceImpl.updatePost(post), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        try {
            postServiceImpl.deletePost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
