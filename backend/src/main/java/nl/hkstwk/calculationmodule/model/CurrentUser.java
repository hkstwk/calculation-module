package nl.hkstwk.calculationmodule.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@Getter
@ToString
public class CurrentUser {
    private final String username;
    private final List<String> roles;
    private final String name;

    public CurrentUser(String username, List<String> roles, String name) {
        this.username = username;
        this.roles = Collections.unmodifiableList(roles);
        this.name = name;
    }
}
