package nl.hkstwk.calculationmodule;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

    public String loadResource(String resourceLocation) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceLocation);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
