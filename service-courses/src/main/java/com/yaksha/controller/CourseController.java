package com.yaksha.controller;

import com.yaksha.entity.CourseEntity;
import com.yaksha.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<CourseEntity> findAll() {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.courseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseEntity course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CourseEntity course, @PathVariable Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.update(course, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        this.courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}