package com.blog.travel.controller;

import com.blog.travel.dto.UserDto;
import com.blog.travel.entity.User;
import com.blog.travel.exception.UserAlreadyExistException;
import com.blog.travel.mapper.UserMapper;
import com.blog.travel.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {
    public static final String NO_USER_FOUND = "No user found.";
    private final UserService userService;
    private final UserMapper mapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper){
        this.userService = userService;
        this.mapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDto userDto) {
        try {
            User savedUser = userService.add(mapper.userDtoToUser(userDto));
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id,
                                             @Valid @RequestBody UserDto userDto,
                                             BindingResult result) {
        validateUpdateFields(result, userDto, id);

        Optional<User> existingUserOpt = userService.get(id);

        if (existingUserOpt.isEmpty()) {
            return new ResponseEntity<>(NO_USER_FOUND, HttpStatus.NOT_FOUND);
        }

        User updatedUser = mapper.updateUserFromUserDto(existingUserOpt.get(), userDto);
        userService.update(updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        Optional<User> optionalUser = userService.get(id);

        return optionalUser.<ResponseEntity<Object>>map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(NO_USER_FOUND, HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        Optional<User> optionalUser = userService.get(id);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(NO_USER_FOUND, HttpStatus.NOT_FOUND);
        }

        userService.delete(id);
        return new ResponseEntity<>("User deleted successfully.", HttpStatus.NO_CONTENT);
    }

    /**
     * Helper method to check validation when updating fields
     */
    private boolean hasErrors(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    /**
     * Validate only non-null fields before updating
     */
    private void validateUpdateFields(BindingResult bindingResult, UserDto userDto, long id) {
        if (hasErrors(bindingResult)) {
            throw new IllegalArgumentException("Invalid input.");
        }

        if (userDto.getName() != null && !userDto.getName().isBlank() && userDto.getName().length() > 50) {
            bindingResult.rejectValue("name", "size", "Name cannot exceed 50 characters.");
        }

        Optional<User> existingUserOpt = userService.get(id);

        if (existingUserOpt.isEmpty()) {
            bindingResult.reject("userNotFound", "Unable to find the specified user.");
            return;
        }

        User existingUser = existingUserOpt.get();

        if (userDto.getEmail() == null || userDto.getEmail().isBlank()
                || userDto.getEmail().contentEquals(existingUser.getEmail())) {
            return;
        }

        if (userService.emailTaken(userDto.getEmail())) {
            bindingResult.rejectValue("email", "unique", "The specified Email already exists.");
        }
    }
}
