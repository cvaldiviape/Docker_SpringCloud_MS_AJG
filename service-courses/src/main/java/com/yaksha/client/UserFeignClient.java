package com.yaksha.client;

import com.yaksha.entity.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="service-users", url="localhost:8001/api/users")
public interface UserFeignClient {

    @GetMapping("/{id}")
    UserDto findById(@PathVariable Long id);

    @PostMapping("/")
    UserDto create(@RequestBody UserDto user);

    @GetMapping("/findAllByListIds")
    List<UserDto> findAllByListIds(@RequestParam Iterable<Long> listIds);

}