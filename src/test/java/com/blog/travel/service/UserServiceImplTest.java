package com.blog.travel.service;

import com.blog.travel.entity.User;
import com.blog.travel.exception.UserAlreadyExistException;
import com.blog.travel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1, user2;

    @BeforeEach
    public void setup(){
        //GIVEN
        user1 =  new User(1L,"user1", "password", "user1@gmail.com");
        user2 =  new User(2L,"user2", "password2", "user2@gmail.com");
    }

    @Test
    void given_user_id_should_return_user_object(){
        //GIVEN
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        //WHEN
        User user = userService.get(user1.getUserId()).orElse(null);

        //THEN
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(1L);
    }

    @Test
    void given_user_to_save_should_save_user() throws UserAlreadyExistException {
        // given - precondition or setup
        given(userRepository.findById(user2.getUserId()))
                .willReturn(Optional.empty());

        given(userService.add(user2)).willReturn(user2);

        System.out.println(userRepository);
        System.out.println(userService);

        // when -  action or the behaviour that we are going test
        User savedUser = userService.add(user2);

        System.out.println(savedUser);
        // then - verify the output
        assertThat(savedUser).isNotNull();
    }

    @Test
    void given_user_to_save_should_throw_exist_exception() {
        given(userRepository.findById(user2.getUserId())).willReturn(Optional.of(user2));

        assertThrows(UserAlreadyExistException.class, () -> userService.add(user2));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void given_user_to_update_should_return_updated_user(){
        given(userRepository.save(user1)).willReturn(user1);
        user1.setEmail("updateEmail");

        User updatedUser = userService.update(user1);

        assertThat(updatedUser.getEmail()).isEqualTo("updateEmail");
    }

    @Test
    void given_user_id_to_delete_then_nothing(){
        long userId = 1L;

        willDoNothing().given(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
