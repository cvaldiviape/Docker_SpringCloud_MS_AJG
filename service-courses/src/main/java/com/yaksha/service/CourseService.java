package com.yaksha.service;

import com.yaksha.entity.CourseEntity;
import com.yaksha.entity.UserDto;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<CourseEntity> findAll();
    Optional<CourseEntity> findById(Long id);
    CourseEntity create(CourseEntity course);
    CourseEntity update(CourseEntity course, Long id) throws Exception;
    void deleteById(Long id) throws Exception;
    Optional<UserDto> assignUser(UserDto user, Long courseId);
    Optional<UserDto> createUser(UserDto user, Long courseId);
    Optional<UserDto> deleteUser(UserDto user, Long courseId);
    Optional<CourseEntity> findByIdWithDataFull(Long id);
    void deleteCourseUserById(Long id);

}