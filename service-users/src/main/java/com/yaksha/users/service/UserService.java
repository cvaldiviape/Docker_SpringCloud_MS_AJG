package com.yaksha.users.service;

import com.yaksha.users.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long id);
    UserEntity create(UserEntity user);
    UserEntity update(UserEntity user, Long id) throws Exception;
    void deleteById(Long id) throws Exception;

}