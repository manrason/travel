package com.blog.travel.repository;

import com.blog.travel.controller.UserController;
import com.blog.travel.dto.UserDto;
import com.blog.travel.entity.User;
import com.blog.travel.exception.UserAlreadyExistException;
import com.blog.travel.mapper.UserMapper;
import com.blog.travel.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class UserControllerTest {

    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = mock(UserService.class);
        userMapper = mock(UserMapper.class);
        userController = new UserController(userService, userMapper);    }

    @Nested
    class RegisterUserTest {

        @Test
        @DisplayName("Successfully registers a new user")
        void successRegisterNewUser() throws UserAlreadyExistException {
            // Arrange
            UserDto userDto = UserDto.builder()
                    .name("user1")
                    .email("user1@gmail.com")
                    .build();

            User savedUser = new User(1L,"user1", "password", "user1@gmail.com");

            when(userMapper.userDtoToUser(userDto)).thenReturn(savedUser);
            when(userService.add(savedUser)).thenReturn(savedUser);

            // Act
            ResponseEntity<Object> response = userController.registerUser(userDto);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            verify(userService, times(1)).add(savedUser);
        }

        @Test
        @DisplayName("Fails registration for existed user")
        void failRegistrationForExistedUser() throws UserAlreadyExistException {
            // Arrange
            UserDto userDto = UserDto.builder()
                    .name("John Doe")
                    .email("john.doe@mail.com")
                    .build();

            when(userMapper.userDtoToUser(userDto)).thenReturn( new User(1L,"John Doe", "password", "john.doe@mail.com"));
            when(userService.add(any(User.class))).thenThrow(new UserAlreadyExistException(""));

            // Act
            ResponseEntity<Object> response = userController.registerUser(userDto);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class UpdateUserTest {

        @Test
        @DisplayName("Successfully updates a user")
        void successUpdateUser() {
            // Arrange
            UserDto userDto = UserDto.builder()
                    .name("user1")
                    .email("user1@gmail.com")
                    .build();

            User existingUser =  new User(1L,"user1", "password", "user1@gmail.com");

            User updatedUser =  new User(1L,"user2", "password", "user2@gmail.com");

            when(userService.get(1L)).thenReturn(Optional.of(existingUser));
            when(userMapper.updateUserFromUserDto(existingUser, userDto)).thenReturn(updatedUser);

            // Act
            BindingResult bindingResult = mock(BindingResult.class);
            ResponseEntity<Object> response = userController.updateUser(1L, userDto, bindingResult);

            // Assert
            verify(userService, times(1)).update(updatedUser);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("Fails update operation due to invalid inputs")
        void failUpdateOperationDueToInvalidInputs() {
            // Arrange
            UserDto userDto = UserDto.builder()
                    .name("Jane Doe")
                    .email("jane.doe@mail.com")
                    .build();

            when(userService.get(1L)).thenReturn(Optional.empty());

            // Act
            BindingResult bindingResult = mock(BindingResult.class);

            ResponseEntity<Object> response = userController.updateUser(1L, userDto, bindingResult);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class GetUserByIdTest {

        @Test
        @DisplayName("Retrieves a user by id")
        void retrieveUserById() {
            // Arrange
            User existingUser =  new User(1L,"user1", "password", "user1@gmail.com");

            when(userService.get(1L)).thenReturn(Optional.of(existingUser));

            // Act
            ResponseEntity<Object> response = userController.getUserById(1L);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("Reports no user found")
        void reportsNoUserFound() {
            // Arrange
            when(userService.get(1L)).thenReturn(Optional.empty());

            // Act
            ResponseEntity<Object> response = userController.getUserById(1L);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class DeleteUserTest {

        @Test
        void testDeleteUserNoUserFound() {
            // Arrange
            when(userService.get((1L))).thenReturn(Optional.empty());

            // Act
            ResponseEntity<Object> response = userController.deleteUser(1L);

            // Assert
            verify(userService, times(1)).get(1L);
            verify(userService, never()).delete(1L);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isEqualTo(UserController.NO_USER_FOUND);
        }

        @Test
        void testDeleteUserDeletedSuccessfully() {
            // Arrange
            User dummyUser = new User(1L, "username", "password", "email@provider.com");
            when(userService.get(1L)).thenReturn(Optional.of(dummyUser));

            // Act
            ResponseEntity<Object> response = userController.deleteUser(1L);

            // Assert
            verify(userService, times(1)).get(1L);
            verify(userService, times(1)).delete(1L);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }
    }
}