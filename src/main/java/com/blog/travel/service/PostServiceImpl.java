package com.blog.travel.service;

import com.blog.travel.entity.Post;
import com.blog.travel.exception.PostAlreadyExistException;
import com.blog.travel.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Optional<Post> getPost(long id){
        return postRepository.findById(id);
    }

    public Post save(Post post) throws PostAlreadyExistException {
        Optional<Post> savedPost = postRepository.findById(post.getId());
        if(savedPost.isPresent()){
            throw new PostAlreadyExistException("Cannot save, post already exist");
        }
        return postRepository.save(post);
    }

    public Post updatePost(Post updatedPost){
        return postRepository.save(updatedPost);
    }

    public void deletePost(long id){
        postRepository.deleteById(id);
    }
}
