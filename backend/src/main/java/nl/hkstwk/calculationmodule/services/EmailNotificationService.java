package nl.hkstwk.calculationmodule.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("email")
@Slf4j
public class EmailNotificationService implements NotificationService {
    public void send(String message, String recipient) {
        log.info("Sending email to {} with message: {}", recipient, message);
    }
}
