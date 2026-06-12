package nl.hkstwk.calculationmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("nl.hkstwk.calculationmodule.config")
public class CalculationModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculationModuleApplication.class, args);
    }

}
