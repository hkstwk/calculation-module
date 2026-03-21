package support;

import org.testcontainers.containers.GenericContainer;

public class DatabaseContainer {

    private static final GenericContainer<?> mysql =
            new GenericContainer<>("mysql:latest")
                    .withExposedPorts(3306)
                    .withEnv("MYSQL_ROOT_PASSWORD", "test")
                    .withEnv("MYSQL_USER", "test")
                    .withEnv("MYSQL_PASSWORD", "test")
                    .withEnv("MYSQL_DATABASE", "calculation-module-db")
                    .withNetwork(E2ENetwork.NETWORK)
                    .withNetworkAliases("mysql");

    static {
        mysql.start();

        String jdbcUrl = "jdbc:mysql://mysql:3306/calculation-module-db";
        System.setProperty("spring.datasource.url", jdbcUrl);
        System.setProperty("spring.datasource.username", "test");
        System.setProperty("spring.datasource.password", "test");
        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
    }

    public static void ensureRunning() {}
}
