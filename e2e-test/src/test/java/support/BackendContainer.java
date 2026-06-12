package support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class BackendContainer {

    private static final Logger logger = LoggerFactory.getLogger(BackendContainer.class);
    private static GenericContainer<?> backend;

    private static void initialize() {
        if (backend == null) {
            String url = System.getProperty("spring.datasource.url");
            String username = System.getProperty("spring.datasource.username");
            String password = System.getProperty("spring.datasource.password");

            if (url == null || username == null || password == null) {
                throw new IllegalStateException("Database properties must be set before initializing BackendContainer. " +
                        "url=" + url + ", username=" + username + ", password=" + password);
            }

            System.out.println("[DEBUG_LOG] Initializing BackendContainer with url: " + url);

            backend = new FixedHostPortGenericContainer<>("hkstwk/calculation-backend:local")
                    .withFixedExposedPort(8080, 8080)
                    .withEnv("SPRING_PROFILES_ACTIVE", "e2e")
                    .withEnv("SPRING_DATASOURCE_URL", url)
                    .withEnv("SPRING_DATASOURCE_USERNAME", username)
                    .withEnv("SPRING_DATASOURCE_PASSWORD", password)
                    .withEnv("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "com.mysql.cj.jdbc.Driver")
                    .withEnv("SPRING_LIQUIBASE_ENABLED", "true")
                    .withEnv("SPRING_JPA_HIBERNATE_DDL_AUTO", "none")
                    .withNetwork(E2ENetwork.NETWORK)
                    .withNetworkAliases("backend")
                    .withLogConsumer(new Slf4jLogConsumer(logger))
                    .waitingFor(Wait.forHttp("/")
                            .forStatusCode(200)
                            .withStartupTimeout(Duration.ofMinutes(2)));

            backend.start();

            System.out.println("[DEBUG_LOG] Backend started and healthy at: http://localhost:8080");
            System.setProperty("backend.url",
                    "http://localhost:" + backend.getMappedPort(8080));
        }
    }

    public static void ensureRunning() {
        initialize();
    }
}
