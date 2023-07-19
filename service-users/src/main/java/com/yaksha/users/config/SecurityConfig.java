package com.yaksha.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/authorized")// defiendo esta ruta
                .permitAll() // como publica, es decir, cualquiera puede acceder

                .antMatchers(HttpMethod.GET, "/", "/{id}")
                .hasAnyAuthority("SCOPE_read", "SCOPE_write") // solo los usuario authenticado y que tenga el scope "read" podran acceder a la ruta "GET" "/"

                .antMatchers(HttpMethod.POST, "/")
                .hasAuthority("SCOPE_write")

                .antMatchers(HttpMethod.PUT, "/{id}")
                .hasAuthority("SCOPE_write")

                .antMatchers(HttpMethod.DELETE, "/{id}")
                .hasAuthority("SCOPE_write")

                .anyRequest() // todas las rutas
                .authenticated()// requieren autenticación
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // que me mantenga la sesion del usuario porque se va emplear TOKEN a travez una APIREST y no en una sesión como si estuviera trabajando con "thymleaf" o MVC
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/service-users-client")) // "service-users-client" lo defini en el "service-auth"
                .oauth2Client(withDefaults())

                .oauth2ResourceServer() // se conectara con el "service-auth" el cual se definio en el "application.yaml" -> "resourceserver"
                .jwt();

        return http.build();
    }

}
