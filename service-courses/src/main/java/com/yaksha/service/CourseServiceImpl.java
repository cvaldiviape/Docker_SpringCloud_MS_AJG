package com.yaksha.service;

import com.yaksha.entity.CourseEntity;
import com.yaksha.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

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


}
