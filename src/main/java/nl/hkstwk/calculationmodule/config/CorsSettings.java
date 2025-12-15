package nl.hkstwk.calculationmodule.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsSettings {
    private boolean allowCredentials = true;
    private List<String> allowedOrigins = new ArrayList<>(List.of("http://localhost:3000", "http://localhost:4200"));
    private List<String> allowedMethods = new ArrayList<>(List.of("*"));
    private List<String> allowedHeaders = new ArrayList<>(List.of("*"));
    private long maxAge = 3600L;
}
