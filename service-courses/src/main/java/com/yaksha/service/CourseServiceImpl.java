package com.yaksha.service;

import com.yaksha.client.UserFeignClient;
import com.yaksha.entity.CourseEntity;
import com.yaksha.entity.CourseUserEntity;
import com.yaksha.entity.UserDto;
import com.yaksha.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    @Transactional(readOnly = true)
    public List<CourseEntity> findAll() {
        return this.courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseEntity> findById(Long id) {
        return this.courseRepository.findById(id);
    }

    @Override
    public CourseEntity create(CourseEntity course) {
        return this.courseRepository.save(course);
    }

    @Override
    public CourseEntity update(CourseEntity course, Long id) throws Exception {
        CourseEntity courseUpdate = this.courseRepository.findById(id).orElseThrow(() -> new Exception("Curso no existe"));
        courseUpdate.setName(course.getName());
        return this.courseRepository.save(courseUpdate);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        CourseEntity courseDelete = this.courseRepository.findById(id).orElseThrow(() -> new Exception("Curso no existe"));
        this.courseRepository.deleteById(courseDelete.getId());
    }

    @Override
    @Transactional
    public Optional<UserDto> assignUser(UserDto user, Long cursoId) {
        Optional<CourseEntity> courseOptional = this.courseRepository.findById(cursoId);
        if (courseOptional.isPresent()) {
            UserDto userFound = userFeignClient.findById(user.getId());
            CourseEntity courseFound = courseOptional.get();
            CourseUserEntity courseUser = new CourseUserEntity();
            courseUser.setUserId(userFound.getId());
            courseFound.addCourseUser(courseUser);
            this.courseRepository.save(courseFound);

            return Optional.of(userFound);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UserDto> createUser(UserDto user, Long courseId) {
        Optional<CourseEntity> courseOptional = this.courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            UserDto userCreated = userFeignClient.create(user);
            CourseEntity courseFound = courseOptional.get();
            CourseUserEntity courseUser = new CourseUserEntity();
            courseUser.setUserId(userCreated.getId());
            courseFound.addCourseUser(courseUser);
            this.courseRepository.save(courseFound);

            return Optional.of(userCreated);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UserDto> deleteUser(UserDto user, Long courseId) {
        Optional<CourseEntity> courseOptional = this.courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            UserDto userFound = this.userFeignClient.findById(user.getId());
            CourseEntity courseFound = courseOptional.get();
            CourseUserEntity courseUser = new CourseUserEntity();
            courseUser.setUserId(userFound.getId());
            courseFound.removeCourseUser(courseUser);
            this.courseRepository.save(courseFound);

            return Optional.of(userFound);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseEntity> findByIdWithDataFull(Long id) {
        Optional<CourseEntity> courseOptional = this.courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            CourseEntity courseFound = courseOptional.get();
            if (!courseFound.getCourseUserList().isEmpty()) {
                List<Long> listIds = courseFound.getCourseUserList()
                        .stream()
                        .map(CourseUserEntity::getUserId)
                        .collect(Collectors.toList());

                List<UserDto> listUsers = this.userFeignClient.findAllByListIds(listIds);
                courseFound.setUsers(listUsers);
            }
            return Optional.of(courseFound);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long id) {
        this.courseRepository.deleteCourseUserByUserId(id);
    }

}