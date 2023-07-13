package com.yaksha.client;

import com.yaksha.entity.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name="service-users", url="${saksa.service.users.url}/api/users")  // es buena practica que el hostname "service-users" se llame igual al mi servicio "service-users"
@FeignClient(name="service-users") // ahora trabaje de esta forma por que estoy usando el DiscoveryCliente de "Kuberbenetes", ademas tambien instale la dependiente en el "pom.xml"
public interface UserFeignClient {

    @GetMapping("/api/users/{id}")
    UserDto findById(@PathVariable Long id);

    @PostMapping("/api/users/")
    UserDto create(@RequestBody UserDto user);

    @GetMapping("/api/users/findAllByListIds")
    List<UserDto> findAllByListIds(@RequestParam Iterable<Long> listIds);

}