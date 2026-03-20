package support;

import org.testcontainers.containers.GenericContainer;

public class BackendContainer {

    private static final GenericContainer<?> backend =
            new GenericContainer<>("hkstwk/calculation-backend:local")
                    .withExposedPorts(8080)
                    .withEnv("SPRING_PROFILES_ACTIVE", "e2e,dev");

    static {
        backend.start();
        System.setProperty("backend.url",
                "http://localhost:" + backend.getMappedPort(8080));
    }

    public static void ensureRunning() {}
}
