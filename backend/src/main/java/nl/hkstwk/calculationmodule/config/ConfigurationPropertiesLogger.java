package nl.hkstwk.calculationmodule.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@Profile("!prod")
@RequiredArgsConstructor
public class ConfigurationPropertiesLogger implements ApplicationListener<ApplicationReadyEvent> {
    private final ConfigurableEnvironment environment;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("=== Active Configuration Properties ===");

        StreamSupport.stream(environment.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                .map(ps -> (EnumerablePropertySource<?>) ps)
                .flatMap(ps -> Arrays.stream(ps.getPropertyNames()))
                .distinct()
                .sorted()
                .forEach(key -> {
                    try {
                        String value = maskSensitive(key, environment.getProperty(key));
                        log.info("[ {} ] = [ {} ]", key, value);
                    } catch (Exception e) {
                        log.warn("[ {} ] = [ERROR: {}]", key, e.getMessage());
                    }
                });

        log.info("=== End Configuration Properties ===");
    }

    private String maskSensitive(String key, String value) {
        String lowerKey = key.toLowerCase();
        if (lowerKey.contains("password") || lowerKey.contains("secret")
                || lowerKey.contains("token") || lowerKey.contains("key")) {
            return "***MASKED***";
        }
        return value;
    }
}