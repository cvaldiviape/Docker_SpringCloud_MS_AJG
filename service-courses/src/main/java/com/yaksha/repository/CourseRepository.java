package com.yaksha.repository;

import com.yaksha.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository <CourseEntity, Long> {

}
