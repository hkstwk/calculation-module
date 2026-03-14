package nl.hkstwk.calculationmodule.utils;

import lombok.experimental.UtilityClass;
import nl.hkstwk.calculationmodule.model.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@UtilityClass
public class UserUtil {

    public static CurrentUser fetchCurrentUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return new CurrentUser(username, roles, authentication.getName());
    }
}
