package com.blog.travel.mapper;

import com.blog.travel.dto.UserDto;
import com.blog.travel.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDtoToUser(UserDto source);
    User updateUserFromUserDto(User source, UserDto userDto);
}
