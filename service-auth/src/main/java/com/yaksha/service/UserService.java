package com.yaksha.service;

import com.yaksha.entity.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private WebClient.Builder webClient;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            // haciendo una petición GET a la url "http://service-users/api/users/login"
            UserDto user = this.webClient.build()
                    .get()
                    .uri("http://service-users/api/users/login", uri -> uri.queryParam("email", email).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(UserDto.class)
                    .block();

            this.logger.info("Usuario email: " + user.getEmail());
            this.logger.info("Usuario name: " + user.getName());
            this.logger.info("Usuario password: " + user.getPassword());

            Set<SimpleGrantedAuthority> listRoles = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")); // "SimpleGrantedAuthority" es un clase se spring security que representa un ROLE o AUTORIDAD
            return new User(email, user.getPassword(), true, true, true, true, listRoles);
        }catch (RuntimeException e) {
            String messageError = "Error en el login, no existe el usuario " + email + " en el sistema";
            this.logger.error(messageError);
            this.logger.error(e.getMessage());
            throw new UsernameNotFoundException(messageError);
        }
    }

}