package nl.hkstwk.calculationmodule.config;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BucketRegistry {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public void registerBucket(String key, Bucket bucket) {
        buckets.put(key, bucket);
    }

    public Bucket getBucket(String key) {
        return buckets.get(key);
    }

    @Override
    public String toString() {
        return "BucketRegistry{" +
                "buckets=" + buckets +
                '}';
    }
}
