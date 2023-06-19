package com.yaksha.users.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

                                        // "saksa.service.courses.url" variable que definimos en "application.properties"
@FeignClient(name="service-courses", url="${saksa.service.courses.url}/api/courses") // es buena practica que el hostname "service-courses" se llame igual al mi servicio "service-courses"
public interface CourseFeignClient {

    @DeleteMapping("/delete-courseUser/{id}")
    void deleteCourseUserById(@PathVariable Long id);

}