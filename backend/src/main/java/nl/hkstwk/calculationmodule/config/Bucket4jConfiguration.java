package nl.hkstwk.calculationmodule.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.jdbc.PrimaryKeyMapper;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.mysql.Bucket4jMySQL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class Bucket4jConfiguration {

    @Bean
    public BucketConfigurationProperties bucketConfigurationProperties(MainConfigurationProperties mainConfigurationProperties) {
        return mainConfigurationProperties.bucket4j();
    }

    @Bean
    public ProxyManager<String> proxyManager(DataSource dataSource, BucketConfigurationProperties properties) {
        return Bucket4jMySQL
                .selectForUpdateBasedBuilder(dataSource)
                .primaryKeyMapper(PrimaryKeyMapper.STRING)
                .expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(properties.expirationAfterWrite()))
                .build();
    }
}
