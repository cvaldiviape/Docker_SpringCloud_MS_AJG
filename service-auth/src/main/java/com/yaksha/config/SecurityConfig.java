package com.yaksha.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    @Autowired
    private Environment env;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Este método define un filtro de seguridad encargado del manejo de errores relacionados con el servidor de autorización. Configura la seguridad
    // del servidor de autorización OAuth2 y habilita OpenID Connect 1.0. Además, establece una redirección a la página de inicio de sesión cuando no
    // se está autenticado desde el punto final de autorización y acepta tokens de acceso para User Info y/o Client Registration.
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login"))
                );

        return http.build();
    }

    // Este método define un filtro de seguridad por defecto. Configura que todas las peticiones HTTP requieran autenticación y utiliza el formulario de inicio
    // de sesión predeterminado. Este filtro se encarga de manejar la redirección a la página de inicio de sesión desde la cadena de filtros del servidor de
    // autorización.
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults()).csrf() .disable();

        return http.build();
    }

    // Esta función de configuración se utiliza para definir cómo se autenticarán los usuarios en el sistema. Establece el UserDetailsService para cargar los
    // detalles del usuario desde una fuente de datos y el PasswordEncoder para codificar y comparar las contraseñas de manera segura durante el proceso de
    // autenticación. La configuración definida en esta función se utilizará para construir el AuthenticationManager, que es esencial para el funcionamiento
    // del sistema de autenticación de Spring Security.
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    // Este método crea un servicio de detalles de usuario en memoria. Se utiliza para proporcionar los detalles de autenticación de un usuario, en este caso,
    // se crea un usuario con nombre de usuario, contraseña y roles.
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("12345")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails);
//    }

    // Este método crea un repositorio de clientes (Angular, ReactJS, VueJS, etc) registrados en memoria. Define un cliente OAuth2 registrado con un ID, cliente
    // secreto, método de autenticación, tipos de concesión, URI de redirección y configuraciones adicionales.
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("user-client") // "user-client" es el mismo que se definio en "service-users" en su archivo "application.yaml"
//                .clientSecret("{noop}12345") // "noop" indica que no esta encriptado
                .clientSecret(passwordEncoder().encode("12345"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri(this.env.getProperty("LB_USER_URI") + "/login/oauth2/code/service-users-client") // "service-users-client" es el mismo que se definio "service-users" en su archivo "application.yaml"
                .redirectUri(this.env.getProperty("LB_USER_URI") + "/api/users/authorized")
                .scope(OidcScopes.OPENID)
                .scope("read")
                .scope("write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    // Este método genera un par de claves RSA y crea un origen JWK (JSON Web Key) con la clave pública y privada generadas. El origen JWK se utiliza para descifrar
    // los tokens JWT (JSON Web Token).
    // Una clave RSA es un par de claves criptográficas asimétricas que consta de una clave pública utilizada para cifrar datos y una clave privada utilizada para
    // descifrar datos y firmar digitalmente.
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    // Este método auxiliar genera un par de claves RSA con un tamaño de 2048 bits.
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().build();
    }

}