package support;

import org.testcontainers.containers.GenericContainer;

public class AngularContainer {

    private static final GenericContainer<?> angular =
            new GenericContainer<>("calculation-frontend-server-e2e:local")
                    .withExposedPorts(8080);

    static {
        angular.start();
        System.setProperty("angular.url",
                "http://localhost:" + angular.getMappedPort(8080) + "/compound");
    }

    public static void ensureRunning() {}
}
