package com.blog.travel.mapper;

import com.blog.travel.dto.UserDto;
import com.blog.travel.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

/*    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void userDtoToUser_GivenSourceNotNull_CreatesUserObject() {
        // arrange
        UserDto userDto = new UserDto();
        userDto.setConfirmPassword("password");

        // act
        User user = userMapper.userDtoToUser(userDto);

        // assert
        assertNotNull(user);
        assertEquals(userDto.getConfirmPassword(), user.getPassword());
    }

    @Test
    void userDtoToUser_GivenSourceIsNull_ReturnsNull() {
        // act
        User user = userMapper.userDtoToUser(null);

        // assert
        assertNull(user);
    }

    @Test
    void updateUserFromUserDto_GivenNonEmptyUserDto_UpdatesUser() {
        // arrange
        User source = new User();
        UserDto userDto = new UserDto();
        userDto.setConfirmPassword("password");
        userDto.setEmail("email@example.com");

        // act
        userMapper.updateUserFromUserDto(source, userDto);

        // assert
        assertEquals(userDto.getConfirmPassword(), source.getPassword());
        assertEquals(userDto.getEmail(), source.getEmail());
    }

    @Test
    void updateUserFromUserDto_GivenEmptyUserDto_DoesntChangeUser() {
        // arrange
        User source = new User();
        UserDto userDto = new UserDto();

        // act
        userMapper.updateUserFromUserDto(source, userDto);

        // assert
        assertNull(source.getPassword());
        assertNull(source.getEmail());
    }

    @Test
    void updateUserFromUserDto_GivenUserDtoWithoutConfirmPassword_DoesntChangeUsersPassword() {
        // arrange
        User source = new User();
        UserDto userDto = new UserDto();
        userDto.setEmail("email@example.com");

        // act
        userMapper.updateUserFromUserDto(source, userDto);

        // assert
        assertNull(source.getPassword());
        assertEquals(userDto.getEmail(), source.getEmail());
    }*/
}
