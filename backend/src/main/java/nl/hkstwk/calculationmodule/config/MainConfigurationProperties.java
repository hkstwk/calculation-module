package nl.hkstwk.calculationmodule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Currency;

@EnableConfigurationProperties({
        MainConfigurationProperties.class,
})
@ConfigurationProperties(prefix = "calculation.module")
public record MainConfigurationProperties(
        @DefaultValue("EUR")
        Currency currency,

        @NestedConfigurationProperty
        NestedConfigurationProperties compound,

        @NestedConfigurationProperty
        BucketConfigurationProperties bucket4j
) {
}
