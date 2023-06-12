package com.yaksha.repository;

import com.yaksha.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository <CourseEntity, Long> {

    @Modifying // solo usar cuando empleamos "delete" o "insert" en la query.
    @Query("delete from CourseUserEntity cu where cu.userId=?1")
    void deleteCourseUserByUserId(Long userId);

}