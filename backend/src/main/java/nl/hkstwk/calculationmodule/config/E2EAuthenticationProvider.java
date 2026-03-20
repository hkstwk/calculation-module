package nl.hkstwk.calculationmodule.config;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("e2e")
public class E2EAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) {
        // In e2e accepteren we altijd de mock-user
        return new E2EAuthenticationToken(
                "e2e-user",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return E2EAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
