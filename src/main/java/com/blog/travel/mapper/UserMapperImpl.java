package com.blog.travel.mapper;

import com.blog.travel.dto.UserDto;
import com.blog.travel.entity.User;

public class UserMapperImpl implements UserMapper {
    @Override
    public User userDtoToUser(UserDto source) {
        if ( source == null ) {
            return null;
        }
        User target = new User();
        target.setPassword(source.getConfirmPassword());
        return target;
    }

    @Override
    public User updateUserFromUserDto(User source, UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }
        if (userDto.getConfirmPassword() != null) {
            source.setPassword(userDto.getConfirmPassword());
            source.setEmail(userDto.getEmail());
        }
        return source;
    }
}
