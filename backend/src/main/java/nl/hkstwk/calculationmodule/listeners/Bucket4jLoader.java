package nl.hkstwk.calculationmodule.listeners;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.TokensInheritanceStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class Bucket4jLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final ProxyManager<String> proxyManager;
    private final Map<String, Bucket> buckets = new HashMap<>();

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        BucketConfiguration bucketConfiguration = BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(2)
                                .refillIntervally(1, Duration.ofMinutes(1)))
                .build();

        buckets.put("calculation", proxyManager.builder().build("calculation", () -> bucketConfiguration));

        Optional<BucketConfiguration> currentBucketConfigOptional = proxyManager.getProxyConfiguration("calculation");

        if (currentBucketConfigOptional.isPresent() && currentBucketConfigOptional.get().equals(bucketConfiguration)) {
            log.info("Bucket configuration for 'calculation' is correctly set.");
        } else {
            log.error("Bucket configuration for 'calculation' does not match expected configuration. replacing...");
            buckets.get("calculation").replaceConfiguration(bucketConfiguration, TokensInheritanceStrategy.ADDITIVE);
        }

        log.info("tryConsume: {}", buckets.get("calculation").tryConsume(1)); // Test consume to ensure bucket is working

        log.info("Bucket config: {}", bucketConfiguration);
        log.info("Bucket config: {}", proxyManager.getProxyConfiguration("calculation"));
        log.info("Bucket state: {}", buckets.get("calculation").getAvailableTokens());
        log.info("Buckets: {}", buckets);
    }
}
