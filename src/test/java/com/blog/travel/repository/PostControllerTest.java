package com.blog.travel.repository;

import com.blog.travel.controller.PostController;
import com.blog.travel.entity.Post;
import com.blog.travel.service.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {
    @MockBean
    private PostServiceImpl postService;

    @Autowired
    private MockMvc mockMvc;

    private List<Post> postList;

    @BeforeEach
    void setup(){
        postList = new ArrayList<>();
        postList.add(new Post(1L, "titre", "une description", "auteur", LocalDate.now()));
        postList.add(new Post(2L, "titre2", "deux description", "auteur2", LocalDate.now()));
    }



    @Test
    void should_return_list_of_posts_and_return_200() throws Exception {

        given(postService.getAllPost()).willReturn(postList);

        mockMvc.perform(get("/api/post/get"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void should_return_post_when_get_post_by_id_and_return_status_ok() throws Exception {
        Post post = new Post(1L, "titre", "une description", "auteur", LocalDate.now());
        when(postService.getPost(post.getId())).thenReturn(Optional.of(post));

        mockMvc.perform(get("/api/post/get/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void should_return_404_when_find_post_by_wrong_id() throws Exception {
        final long usedId = 5L;
        given(postService.getPost(usedId)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/get/{id}", usedId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_created_when_add_post() throws Exception {
        Post post = new Post(3L, "titleToAdd", "descriptionToAdd", "auteur3", LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/add/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                
                                """))
                .andExpect(status().isCreated());
    }
}
