package nl.hkstwk.calculationmodule.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsSettings corsSettings;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(corsSettings.isAllowCredentials());
        corsSettings.getAllowedOrigins().forEach(config::addAllowedOrigin);
        corsSettings.getAllowedMethods().forEach(config::addAllowedMethod);
        corsSettings.getAllowedHeaders().forEach(config::addAllowedHeader);
        config.setMaxAge(corsSettings.getMaxAge());

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}