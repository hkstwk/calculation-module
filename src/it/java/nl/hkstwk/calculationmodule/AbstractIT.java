package nl.hkstwk.calculationmodule;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ActiveProfiles("it")
@SpringBootTest
@AutoConfigureMockMvc
public class AbstractIT {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ResourceLoader resourceLoader;

    protected static MySQLContainer<?> mysqlContainer;

    @BeforeAll
    static void setUp() {
        // Initialize and start the MySQL container
        mysqlContainer = new MySQLContainer<>("mysql:8.0.36")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        mysqlContainer.start();

        // Set system properties for Spring DataSource configuration
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");

        // Container information for debugging
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

    public String loadResource(String resourceLocation) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceLocation);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
