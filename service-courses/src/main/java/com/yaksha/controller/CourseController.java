package com.yaksha.controller;

import com.yaksha.entity.CourseEntity;
import com.yaksha.entity.UserDto;
import com.yaksha.service.CourseService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
    public ResponseEntity<?> findById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = true) String token) {
//        return ResponseEntity.ok(this.courseService.findById(id));
        return ResponseEntity.ok(this.courseService.findByIdWithDataFull(id, token)); // para mostar un curso con sus "users"
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

    @PutMapping("/assign-user/{courseId}")
    public ResponseEntity<?> assignUser(@RequestBody UserDto user, @PathVariable Long courseId, @RequestHeader(value = "Authorization", required = true) String token) { // va asignar un Usuario a un Curso
        Optional<UserDto> userOptional;
        try {
            userOptional = this.courseService.assignUser(user, courseId, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por el id o error en la comunicacion: " + e.getMessage()));
        }
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser(@RequestBody UserDto user, @PathVariable Long courseId, @RequestHeader(value = "Authorization", required = true) String token) { // va crear y asignar un usuario a un Curso
        Optional<UserDto> userOptional;
        try {
            userOptional = this.courseService.createUser(user, courseId, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No se pudo crear el usuario o error en la comunicacion: " + e.getMessage()));
        }
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{courseId}")
    public ResponseEntity<?> deleteUser(@RequestBody UserDto user, @PathVariable Long courseId, @RequestHeader(value = "Authorization", required = true) String token) { // va eliminar un Usuario de un Curso
        Optional<UserDto> userOptional;
        try {
            userOptional = this.courseService.deleteUser(user, courseId, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe el usuario por el id o error en la comunicacion: " + e.getMessage()));
        }
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-courseUser/{id}")
    public ResponseEntity<?> deleteCourseUserById(@PathVariable Long id){
        this.courseService.deleteCourseUserById(id);
        return ResponseEntity.noContent().build();
    }

}