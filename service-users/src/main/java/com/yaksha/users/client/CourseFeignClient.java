package com.yaksha.users.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="service-courses", url="host.docker.internal:8002/api/courses")
public interface CourseFeignClient {

    @DeleteMapping("/delete-courseUser/{id}")
    void deleteCourseUserById(@PathVariable Long id);

}