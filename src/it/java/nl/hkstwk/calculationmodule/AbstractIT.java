package nl.hkstwk.calculationmodule;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@ActiveProfiles("it")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIT {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ResourceLoader resourceLoader;

    public String loadResource(String resourceLocation) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceLocation);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    protected static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtWithSubjectAndAuthority(String subject, String role) {
        return jwt().jwt(jwtBuilder -> jwtBuilder
                        .subject(subject)
                        .claim("https://yourapp.example.com/roles",List.of(role)))
                .authorities(new SimpleGrantedAuthority(String.format("ROLE_%s", role)));
    }
}
