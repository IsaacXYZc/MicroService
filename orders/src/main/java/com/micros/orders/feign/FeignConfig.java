package com.micros.orders.feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor authorizationInterceptor() {
        return template -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getCredentials() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getCredentials();
                String tokenValue = jwt.getTokenValue(); // Extraer el valor del token
                template.header("Authorization", "Bearer " + tokenValue);
                System.out.println("Token añadido: " + tokenValue); // Para depuración
            } else {
                System.out.println("No se encontró un token JWT en las credenciales.");
            }
        };
    }
}