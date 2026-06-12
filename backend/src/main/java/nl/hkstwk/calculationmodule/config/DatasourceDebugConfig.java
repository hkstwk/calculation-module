package nl.hkstwk.calculationmodule.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceDebugConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @PostConstruct
    void logUrl() {
        System.out.println("### EFFECTIVE spring.datasource.url = " + url);
    }
}

