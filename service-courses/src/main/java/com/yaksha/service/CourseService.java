package com.yaksha.service;

import com.yaksha.entity.CourseEntity;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<CourseEntity> findAll();
    Optional<CourseEntity> findById(Long id);
    CourseEntity create(CourseEntity course);
    CourseEntity update(CourseEntity course, Long id) throws Exception;
    void deleteById(Long id) throws Exception;

}