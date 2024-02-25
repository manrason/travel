package com.blog.travel.repository;

import com.blog.travel.controller.PostController;
import com.blog.travel.entity.Post;
import com.blog.travel.exception.PostAlreadyExistException;
import com.blog.travel.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class PostControllerTest {

    private PostController postController;
    private PostService postService;

    @BeforeEach
    public void setup() {
        postService = mock(PostService.class);
        postController = new PostController(postService);
    }

    @Test
    void getAllPosts_Ok() {
        // Setup
        when(postService.getAllPost()).thenReturn(new ArrayList<>());

        // Execute
        ResponseEntity<List<Post>> result = postController.getAllPosts();

        // Verify
        verify(postService, times(1)).getAllPost();
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.OK));
    }

    @Test
    void getAllPosts_ServerError() {
        // Setup
        when(postService.getAllPost()).thenThrow(RuntimeException.class);

        // Execute
        ResponseEntity<List<Post>> result = postController.getAllPosts();

        // Verify
        verify(postService, times(1)).getAllPost();
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(result.getBody(), nullValue());
    }

    @Test
    void getPost_Ok() {
        // Setup
        long postId = 1;
        Post post = mock(Post.class);
        when(postService.getPost(postId)).thenReturn(Optional.of(post));

        // Execute
        ResponseEntity<Post> result = postController.getPost(postId);

        // Verify
        verify(postService, times(1)).getPost(postId);
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.OK));
        assertThat(result.getBody(), sameInstance(post));
    }

    @Test
    void getPost_NotFound() {
        // Setup
        long postId = 1;
        when(postService.getPost(postId)).thenReturn(Optional.empty());

        // Execute
        ResponseEntity<Post> result = postController.getPost(postId);

        // Verify
        verify(postService, times(1)).getPost(postId);
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.NOT_FOUND));
        assertThat(result.getBody(), nullValue());
    }

    @Test
    void save_Created() throws PostAlreadyExistException {
        // Setup
        Post post = mock(Post.class);
        when(postService.save(any(Post.class))).thenReturn(post);

        // Execute
        ResponseEntity<Post> result = postController.save(post);

        // Verify
        verify(postService, times(1)).save(any(Post.class));
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.CREATED));
        assertThat(result.getBody(), sameInstance(post));
    }

/*    @Test
    void save_InternalServerError() throws PostAlreadyExistException {
        // Setup
        Post post = mock(Post.class);
        when(postService.save(any(Post.class))).thenThrow(RuntimeException.class);

        // Execute
        ResponseEntity<Post> result = postController.save(post);

        // Verify
        verify(postService, times(1)).save(any(Post.class));
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(result.getBody(), nullValue());
    }*/

    @Test
    void update_Ok() {
        // Setup
        long postId = 1;
        Post post = mock(Post.class);
        when(postService.updatePost(post)).thenReturn(post);

        // Execute
        ResponseEntity<Post> result = postController.update(post, postId);

        // Verify
        verify(postService, times(1)).updatePost(post);
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.OK));
        assertThat(result.getBody(), sameInstance(post));
    }

    @Test
    void delete_NoContent() {
        // Setup
        long postId = 1;
        doNothing().when(postService).deletePost(postId);

        // Execute
        ResponseEntity<HttpStatus> result = postController.delete(postId);

        // Verify
        verify(postService, times(1)).deletePost(postId);
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.NO_CONTENT));
    }

    @Test
    void delete_InternalServerError() {
        // Setup
        long postId = 1;
        doThrow(RuntimeException.class).when(postService).deletePost(postId);

        // Execute
        ResponseEntity<HttpStatus> result = postController.delete(postId);

        // Verify
        verify(postService, times(1)).deletePost(postId);
        assertThat(result.getStatusCode(), sameInstance(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
