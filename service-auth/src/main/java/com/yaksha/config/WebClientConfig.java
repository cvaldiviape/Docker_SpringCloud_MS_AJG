package com.yaksha.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // "@LoadBalanced": WebClient estará configurado para utilizar un mecanismo de equilibrio de carga cuando realice solicitudes HTTP, lo que permite distribuir la carga de las
    // solicitudes a diferentes instancias de un servicio en un entorno de microservicios.
    @LoadBalanced
    @Bean
    WebClient.Builder webClient(){
        return WebClient.builder(); // En este método, se construye y configura el WebClient utilizando el patrón de diseño Builder. El WebClient es una clase de Spring WebFlux
                                    // que se utiliza para realizar solicitudes HTTP de forma reactiva. En esta configuración, el WebClient se crea con la configuración predeterminada.
    }

}
