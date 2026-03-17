package support;

import org.testcontainers.containers.GenericContainer;

public class BackendContainer {

    private static final GenericContainer<?> backend =
            new GenericContainer<>("hkstwk/calculation-module:latest")
                    .withExposedPorts(8080);

    static {
        backend.start();
        System.setProperty("backend.url",
                "http://localhost:" + backend.getMappedPort(8080));
    }

    public static void ensureRunning() {}
}
