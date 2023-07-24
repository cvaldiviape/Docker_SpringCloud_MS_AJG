package com.yaksha.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/api/users/authorized", "/api/users/login")// defiendo esta ruta
                .permitAll() // como publica, es decir, cualquiera puede acceder

                .antMatchers(HttpMethod.GET, "/api/users/", "/api/users/{id}")
                .hasAnyAuthority("SCOPE_read", "SCOPE_write") // solo los usuario authenticado y que tenga el scope "read" podran acceder a la ruta "GET" "/"

                .antMatchers(HttpMethod.POST, "/api/users/")
                .hasAuthority("SCOPE_write")

                .antMatchers(HttpMethod.PUT, "/api/users/{id}")
                .hasAuthority("SCOPE_write")

                .antMatchers(HttpMethod.DELETE, "/api/users/{id}")
                .hasAuthority("SCOPE_write")

                .anyRequest() // todas las rutas
                .authenticated()// requieren autenticación
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // que me mantenga la sesion del usuario porque se va emplear TOKEN a travez una APIREST y no en una sesión como si estuviera trabajando con "thymleaf" o MVC
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/service-users-client")) // "service-users-client" lo defini en el "service-auth"
                .oauth2Client(withDefaults())

                // "CSRF" se utiliza para deshabilitar la protección CSRF (Cross-Site Request Forgery) en una aplicación web. CSRF es un tipo de ataque en el que un atacante puede hacer que un usuario autenticado
                // realice acciones no deseadas en una aplicación web sin su conocimiento o consentimiento. Para protegerse contra este tipo de ataques, Spring Security habilita la protección CSRF de forma
                // predeterminada.
                // Sin embargo, hay situaciones en las que deshabilitar CSRF puede ser necesario o adecuado:
                // 1- API RESTful: Cuando estás construyendo una API RESTful que se utiliza principalmente como backend de una aplicación de una sola página (SPA) o una aplicación móvil, no es necesario
                //                 aplicar protección CSRF, ya que la autenticación se realiza mediante tokens (como JWT) en lugar de sesiones.
                // 2- Aplicaciones Stateless: Algunas aplicaciones pueden ser diseñadas como "stateless" (sin estado), lo que significa que no utilizan sesiones y no requieren protección CSRF.
                // 3- Otras medidas de seguridad: En ciertos casos, es posible que la aplicación tenga otras medidas de seguridad en su lugar que mitiguen los riesgos de CSRF, y por lo tanto, se toma la
                //                                decisión de deshabilitar la protección CSRF.
                // EJEMPLOS DE ATAQUES: "Cambio de Contraseña No Deseado", "Realizar una Transferencia No Autorizada",
                .csrf().disable() // LO COLOQUE EN EL "service-auth" en su "SecurityConfig"

                .oauth2ResourceServer() // se conectara con el "service-auth" el cual se definio en el "application.yaml" -> "resourceserver"
                .jwt();

        return http.build();
    }

}
