package com.yaksha.users.mapper;

import com.yaksha.users.dto.UserDto;
import com.yaksha.users.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserDto userDto);

}