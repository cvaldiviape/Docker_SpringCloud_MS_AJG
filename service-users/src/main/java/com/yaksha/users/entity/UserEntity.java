package com.yaksha.users.entity;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotEmpty
    @Email
    @Column(name = "email", unique = true)
    private String email;
    @NotBlank
    @Column(name = "password")
    private String password;

}