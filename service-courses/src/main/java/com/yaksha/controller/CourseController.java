package com.yaksha.controller;

import com.yaksha.entity.CourseEntity;
import com.yaksha.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> create(@Valid @RequestBody CourseEntity course, BindingResult result) {
        if (result.hasErrors()) {
            return showError(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CourseEntity course, BindingResult result, @PathVariable Long id) throws Exception {
        if (result.hasErrors()) {
            return showError(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.update(course, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        this.courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> showError(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}