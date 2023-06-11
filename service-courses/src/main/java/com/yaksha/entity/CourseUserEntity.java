package com.yaksha.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses_users")
public class CourseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;
    @NotEmpty
    @Column(name = "user_id", unique = true)
    private Long userId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CourseUserEntity)) {
            return false;
        }
        CourseUserEntity o = (CourseUserEntity) obj;
        return this.userId != null && this.userId.equals(o.userId);
    }

}