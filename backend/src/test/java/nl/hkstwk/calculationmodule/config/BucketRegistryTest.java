package nl.hkstwk.calculationmodule.config;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class BucketRegistryTest {

    private static final String KEY = "user-123";
    private static final String UNKNOWN_KEY = "non-existing-key";

    private BucketRegistry bucketRegistry;

    @BeforeEach
    void setUp() {
        bucketRegistry = new BucketRegistry();
    }

    @Test
    @DisplayName("getBucket returns the bucket previously registered under the same key")
    void getBucketReturnsRegisteredBucket() {
        Bucket bucket = Mockito.mock(Bucket.class);

        bucketRegistry.registerBucket(KEY, bucket);

        assertThat(bucketRegistry.getBucket(KEY)).isSameAs(bucket);
    }

    @Test
    @DisplayName("getBucket returns null when no bucket is registered for the key")
    void getBucketReturnsNullForUnknownKey() {
        assertThat(bucketRegistry.getBucket(UNKNOWN_KEY)).isNull();
    }

    @Test
    @DisplayName("registerBucket overwrites the bucket when the same key is registered twice")
    void registerBucketOverwritesExistingBucket() {
        Bucket firstBucket = Mockito.mock(Bucket.class);
        Bucket secondBucket = Mockito.mock(Bucket.class);

        bucketRegistry.registerBucket(KEY, firstBucket);
        bucketRegistry.registerBucket(KEY, secondBucket);

        assertThat(bucketRegistry.getBucket(KEY))
                .isSameAs(secondBucket)
                .isNotSameAs(firstBucket);
    }

    @ParameterizedTest
    @ValueSource(strings = {"alice", "bob", "rate-limit-key", ""})
    @DisplayName("registerBucket and getBucket round-trip for various keys")
    void registerAndGetBucketForVariousKeys(String key) {
        Bucket bucket = Mockito.mock(Bucket.class);

        bucketRegistry.registerBucket(key, bucket);

        assertThat(bucketRegistry.getBucket(key)).isSameAs(bucket);
    }

    @Test
    @DisplayName("toString reflects an empty registry")
    void toStringReflectsEmptyRegistry() {
        assertThat(bucketRegistry.toString())
                .isEqualTo("BucketRegistry{buckets={}}");
    }

    @Test
    @DisplayName("toString includes the registered key")
    void toStringIncludesRegisteredKey() {
        bucketRegistry.registerBucket(KEY, Mockito.mock(Bucket.class));

        assertThat(bucketRegistry.toString())
                .startsWith("BucketRegistry{buckets={")
                .contains(KEY)
                .endsWith("}}");
    }
}
