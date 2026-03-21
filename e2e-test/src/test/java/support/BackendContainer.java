package support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

            backend = new GenericContainer<>("hkstwk/calculation-backend:local")
                    .withExposedPorts(8080)
                    .withEnv("SPRING_PROFILES_ACTIVE", "e2e")
                    .withEnv("SPRING_DATASOURCE_URL", url)
                    .withEnv("SPRING_DATASOURCE_USERNAME", username)
                    .withEnv("SPRING_DATASOURCE_PASSWORD", password)
                    .withEnv("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "com.mysql.cj.jdbc.Driver")
                    .withNetwork(E2ENetwork.NETWORK)
                    .withLogConsumer(new Slf4jLogConsumer(logger))
                    .waitingFor(Wait.forListeningPort()
                            .withStartupTimeout(Duration.ofMinutes(2)));

            backend.start();

            System.setProperty("backend.url",
                    "http://localhost:" + backend.getMappedPort(8080));
        }
    }

    public static void ensureRunning() {
        initialize();
    }
}
