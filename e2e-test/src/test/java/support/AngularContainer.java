package support;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

public class AngularContainer {

    private static final GenericContainer<?> angular =
            new FixedHostPortGenericContainer<>("calculation-frontend:e2e")
                    .withFixedExposedPort(4200, 8080)
                    .withNetwork(E2ENetwork.NETWORK);

    static {
        angular.start();
        System.setProperty("angular.url",
                "http://localhost:" + angular.getMappedPort(8080) + "/compound");
    }

    public static void ensureRunning() {}
}
