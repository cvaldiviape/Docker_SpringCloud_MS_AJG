package com.yaksha.users.controller;

import com.yaksha.users.entity.UserEntity;
import com.yaksha.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserEntity> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserEntity user, BindingResult result) {
        if (result.hasErrors()) {
            return showError(result);
        }
        if(this.userService.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe el correo " + user.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserEntity user, BindingResult result, @PathVariable Long id) throws Exception {
        if (result.hasErrors()) {
            return showError(result);
        }
        if(this.userService.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe el correo " + user.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.update(user, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findAllByListIds")
    public ResponseEntity<?> findAllByListIds(@RequestParam List<Long> listIds){
        return ResponseEntity.ok(this.userService.findAllByListIds(listIds));
    }

    private ResponseEntity<Map<String, String>> showError(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
