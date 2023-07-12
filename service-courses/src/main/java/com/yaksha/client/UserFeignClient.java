package com.yaksha.client;

import com.yaksha.entity.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="service-users", url="${saksa.service.users.url}/api/users")  // es buena practica que el hostname "service-users" se llame igual al mi servicio "service-users"
public interface UserFeignClient {

    @GetMapping("/{id}")
    UserDto findById(@PathVariable Long id);

    @PostMapping("/")
    UserDto create(@RequestBody UserDto user);

    @GetMapping("/findAllByListIds")
    List<UserDto> findAllByListIds(@RequestParam Iterable<Long> listIds);

}