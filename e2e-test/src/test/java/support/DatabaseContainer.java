package support;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class DatabaseContainer {

    private static final GenericContainer<?> mysql =
            new FixedHostPortGenericContainer<>("mysql:latest")
                    .withFixedExposedPort(3306, 3306)
                    .withEnv("MYSQL_ROOT_PASSWORD", "test")
                    .withEnv("MYSQL_USER", "test")
                    .withEnv("MYSQL_PASSWORD", "test")
                    .withEnv("MYSQL_DATABASE", "calculation-module-db")
                    .withNetwork(E2ENetwork.NETWORK)
                    .withNetworkAliases("mysql")
                    .waitingFor(Wait.forListeningPort()
                            .withStartupTimeout(Duration.ofMinutes(2)));

    static {
        mysql.start();

        try {
            // Give MySQL a bit more time to actually start the database process inside the container
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String jdbcUrl = "jdbc:mysql://mysql:3306/calculation-module-db";
        System.out.println("[DEBUG_LOG] Database started at: " + jdbcUrl);
        System.setProperty("spring.datasource.url", jdbcUrl);
        System.setProperty("spring.datasource.username", "test");
        System.setProperty("spring.datasource.password", "test");
        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
    }

    public static void ensureRunning() {}
}
