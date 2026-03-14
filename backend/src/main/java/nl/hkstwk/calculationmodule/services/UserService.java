package nl.hkstwk.calculationmodule.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.entities.User;
import nl.hkstwk.calculationmodule.repositories.UserCrudRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserCrudRepository userCrudRepository;

    public User findCreateOrUpdateUser(String username) {
        return userCrudRepository.findByUsername(username)
                .map(user -> {
                    if (!user.getName().equals(username)) {
                        return updateUser(username, user);
                    }
                    return user;
                })
                .orElseGet(() -> createUser(username));
    }

    private User createUser(String username) {
        log.info("Creating new user {}", username);
        User createdUser = new User(username, username);
        return userCrudRepository.save(createdUser);
    }

    private User updateUser(String username, User user) {
        log.info("Updating user name to {}", username);
        user.setName(username);
        return userCrudRepository.save(user);
    }
}
