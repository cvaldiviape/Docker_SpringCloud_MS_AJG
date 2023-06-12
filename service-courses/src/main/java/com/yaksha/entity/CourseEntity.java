package com.yaksha.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // "orphanRemoval" indicando que si hay algun registro con "user_id" como NULL, entonces que se elimine
    @JoinColumn(name = "course_id")
    private List<CourseUserEntity> courseUserList = new ArrayList<>();
    @Transient
    private List<UserDto> users = new ArrayList<>();

    public void addCourseUser(CourseUserEntity courseUser) {
        courseUserList.add(courseUser);
    }

    public void removeCourseUser(CourseUserEntity courseUser) {
        courseUserList.remove(courseUser);
    }

}