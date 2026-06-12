package nl.hkstwk.calculationmodule.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Profile("e2e")
public class E2EAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Maak een lege token die door de provider wordt ingevuld
        Authentication authRequest = new E2EAuthenticationToken(
                "e2e-user",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        Authentication authResult = SecurityContextHolder.getContext().getAuthentication();

        if (authResult == null) {
            authResult = authRequest;
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }

        filterChain.doFilter(request, response);
    }
}

