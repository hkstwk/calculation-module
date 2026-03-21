package nl.hkstwk.calculationmodule.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@Profile("e2e")
@Slf4j
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain e2eFilterChain(HttpSecurity http,
                                              E2EAuthenticationFilter e2eFilter) throws Exception {
        log.info("The e2e security configuration");
        return http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(e2eFilter, AnonymousAuthenticationFilter.class)
                .build();
    }
}
