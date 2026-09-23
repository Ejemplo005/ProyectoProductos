package com.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación. @SpringBootApplication combina:
 *  - @Configuration
 *  - @EnableAutoConfiguration (activa toda la autoconfiguración de Spring Boot)
 *  - @ComponentScan (busca @Component/@Service/@Repository/@Controller
 *    en este paquete y sus subpaquetes, y los conecta solo)
 */
@SpringBootApplication
public class ProductosSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductosSpringbootApplication.class, args);
    }
}
