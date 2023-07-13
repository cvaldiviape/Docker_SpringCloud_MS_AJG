package com.yaksha.users.controller;

import com.yaksha.users.entity.UserEntity;
import com.yaksha.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
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
    @Autowired
    private ApplicationContext context;
    @Autowired
    private Environment env; // con esto puedo obtener y leer variable de ambiente

    @GetMapping("/crash")
    public void crash(){
        ((ConfigurableApplicationContext)context).close(); // simulando un quiebre de la aplicacion, es decir, simulado que se caiga mi servicio.
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserEntity> listUsers = userService.findAll();

        Map<String, Object> body = new HashMap<>();
        body.put("listUsers", listUsers);
        body.put("podInfo", env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP")); // "MY_POD_NAME" y "MY_POD_IP" son información del POD donde se encuentra mi contenedor
                                                                                                   // "service-users", el cual esta configurador en "deployment-service-users.yaml"
                                                                                                   // https://kubernetes.io/docs/tasks/inject-data-application/environment-variable-expose-pod-information/
        return ResponseEntity.ok(body);
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
