package org.example.stockerp.config; // Ajusta el paquete según tu proyecto

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Permitir credenciales (cookies, headers de auth)
        config.setAllowCredentials(true);

        // 2. Orígenes permitidos: Usamos "AllowedOriginPatterns" con "*"
        // Esto permite CUALQUIER IP pero sigue funcionando con credenciales.
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        // 3. Headers permitidos (Incluyendo los necesarios para preflight y JWT)
        config.setAllowedHeaders(Arrays.asList(
                "Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "X-Requested-With"
        ));

        // 4. Métodos permitidos
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 5. Tiempo de caché para la respuesta preflight (1 hora)
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);

        // TRUCO FINAL: Envolver en FilterRegistrationBean para forzar la prioridad ALTA
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Se ejecuta ANTES que Spring Security
        return bean;
    }
}