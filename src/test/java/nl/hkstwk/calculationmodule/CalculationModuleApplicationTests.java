package nl.hkstwk.calculationmodule;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.driverClassName=org.h2.Driver")
class CalculationModuleApplicationTests {

    @Test
    void contextLoads() {

    }

}
