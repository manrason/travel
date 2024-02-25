package com.blog.travel.service;

import com.blog.travel.entity.Post;
import com.blog.travel.exception.PostAlreadyExistException;
import com.blog.travel.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    private Post post1, post2;

    @BeforeEach
    public void setup(){
        //GIVEN
        post1 =  new Post(1L,"titre", "une description", "auteur", LocalDate.now());
        post2 =  new Post(2L,"titre2", "deux description", "auteur2", LocalDate.now());
    }


    @Test
    void should_return_all_student(){
        //WHEN
        when(postRepository.findAll()).thenReturn(List.of(post1, post2));

        //THEN
        assertThat(postServiceImpl.getAllPost()).isNotNull();
        assertThat(postServiceImpl.getAllPost()).hasSize(2);
    }
    @Test
    void given_post_id_should_return_post_object(){
        //GIVEN
        given(postRepository.findById(1L)).willReturn(Optional.of(post1));
        //WHEN
        Post post = postServiceImpl.getPost(post1.getId()).get();

        //THEN
        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(1L);
    }

    @Test
    void given_post_to_save_should_save_post() throws Exception {
        // given - precondition or setup
        given(postRepository.findById(post2.getId()))
                .willReturn(Optional.empty());

        given(postServiceImpl.save(post2)).willReturn(post2);

        System.out.println(postRepository);
        System.out.println(postServiceImpl);

        // when -  action or the behaviour that we are going test
        Post savedPost = postServiceImpl.save(post2);

        System.out.println(savedPost);
        // then - verify the output
        assertThat(savedPost).isNotNull();
    }

    @Test
    void given_post_to_save_should_throw_exist_exception() {
        given(postRepository.findById(post2.getId())).willReturn(Optional.of(post2));

        assertThrows(PostAlreadyExistException.class, () -> postServiceImpl.save(post2));

        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void given_post_to_update_should_return_updated_post(){
        given(postRepository.save(post1)).willReturn(post1);
        post1.setAuthor("updateAuthor");
        post1.setTitle("updateTitle");

        Post updatedPost = postServiceImpl.updatePost(post1);

        assertThat(updatedPost.getAuthor()).isEqualTo("updateAuthor");
        assertThat(updatedPost.getTitle()).isEqualTo("updateTitle");
    }

    @Test
    void given_post_id_to_delete_then_nothing(){
        long postId = 1L;

        willDoNothing().given(postRepository).deleteById(postId);

        postServiceImpl.deletePost(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }
}
