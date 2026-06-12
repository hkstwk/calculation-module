package nl.hkstwk.calculationmodule.services;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.TokensInheritanceStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.config.BucketConfigurationProperties;
import nl.hkstwk.calculationmodule.config.BucketRegistry;
import nl.hkstwk.calculationmodule.config.MainConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BucketService {
    private final ProxyManager<String> proxyManager;
    private final MainConfigurationProperties mainConfigurationProperties;
    private final BucketRegistry bucketRegistry;

    public Bucket resolveBucket(String key) {
        BucketConfigurationProperties.BucketProperties bucketProperties = mainConfigurationProperties.bucket4j().bucket();
        BucketConfiguration bucketConfiguration = BucketConfiguration.builder()
                .addLimit(limit -> {
                    var limitBuilder = limit.capacity(bucketProperties.capacity());
                    long refillAmount = bucketProperties.refillAmount() != null ? bucketProperties.refillAmount() : bucketProperties.capacity();
                    if (bucketProperties.refillType() == BucketConfigurationProperties.RefillType.GREEDY) {
                        return limitBuilder.refillGreedy(refillAmount, bucketProperties.refillInterval());
                    } else {
                        return limitBuilder.refillIntervally(refillAmount, bucketProperties.refillInterval());
                    }
                })
                .build();

        Bucket bucket = proxyManager.builder().build(key, () -> bucketConfiguration);
        bucketRegistry.registerBucket(key, bucket);
        log.info("Bucket registered: {}", bucket);

        Optional<BucketConfiguration> currentBucketConfigOptional = proxyManager.getProxyConfiguration(key);

        if (currentBucketConfigOptional.isPresent() && currentBucketConfigOptional.get().equals(bucketConfiguration)) {
            log.info("Bucket configuration for 'calculation' is correctly set.");
        } else {
            log.error("Bucket configuration for 'calculation' does not match expected configuration. replacing...");
            bucketRegistry.getBucket(key).replaceConfiguration(bucketConfiguration, TokensInheritanceStrategy.ADDITIVE);
        }

        return bucket;
    }
}