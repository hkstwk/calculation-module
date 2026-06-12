package nl.hkstwk.calculationmodule.config;

import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.RoundingMode;

public record NestedConfigurationProperties(
        boolean enabled,
        @DefaultValue("HALF_UP")
        RoundingMode roundingMode
) {
}
