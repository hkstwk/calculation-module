package support;

import org.testcontainers.containers.GenericContainer;

public class DatabaseContainer {

    private static final GenericContainer<?> mysql =
            new GenericContainer<>("mysql:latest")
                    .withExposedPorts(3316)
                    .withEnv("SPRING_DATASOURCE_URL", System.getProperty("spring.datasource.url"))
                    .withEnv("MYSQL_ROOT_PASSWORD", "test")
                    .withEnv("MYSQL_USER", "test")
                    .withEnv("MYSQL_PASSWORD", "test")
                    .withEnv("MYSQL_DATABASE", "calculation-module-db");

    static {
        mysql.start();

        String jdbcUrl = "jdbc:mysql://localhost:" +
                mysql.getMappedPort(3316) +
                "/calculation-module-db";

        System.setProperty("spring.datasource.url", jdbcUrl);
        System.setProperty("spring.datasource.username", "test");
        System.setProperty("spring.datasource.password", "test");
        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
    }

    public static void ensureRunning() {}
}
