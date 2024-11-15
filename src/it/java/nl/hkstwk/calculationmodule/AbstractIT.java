package nl.hkstwk.calculationmodule;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles("it")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIT {
    @Autowired
    protected MockMvc mockMvc;

    protected static MySQLContainer<?> mysqlContainer;

    @Value("${test.json.directory}")
    protected String jsonDirectory;

    @BeforeAll
    static void setUp() {
        // Initialize and start the MySQL container
        mysqlContainer = new MySQLContainer<>("mysql:latest")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");

        mysqlContainer.start();

        // Set system properties for Spring DataSource configuration
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");

        // Optional: Print container information for debugging
        System.out.println("MySQL container started at: " + mysqlContainer.getJdbcUrl());
        System.out.println("Username: " + mysqlContainer.getUsername());
        System.out.println("Password: " + mysqlContainer.getPassword());
    }

    @AfterAll
    static void tearDown() {
        if (mysqlContainer != null) {
            mysqlContainer.stop();
        }
    }

}
