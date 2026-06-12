package nl.hkstwk.calculationmodule.config;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Duration;

@NullMarked
public record BucketConfigurationProperties(
        BucketProperties bucket,
        Duration expirationAfterWrite
) {
    public record BucketProperties (
            @NotBlank String key,
            @NotNull Duration refillInterval,
            @NotNull Long capacity,
            @NotNull RefillType refillType,
            @Nullable Long refillAmount
    ) {
    }

    public enum RefillType {
        GREEDY, INTERVALLY
    }
}
