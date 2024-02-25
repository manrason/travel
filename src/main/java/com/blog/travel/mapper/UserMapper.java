package com.blog.travel.mapper;

import com.blog.travel.dto.UserDto;
import com.blog.travel.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    default User userDtoToUser(UserDto source) {
        if ( source == null ) {
            return null;
        }
        User target = new User();
        target.setPassword(source.getConfirmPassword());
        return target;
    }
    default User updateUserFromUserDto(User source, UserDto userDto){
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
