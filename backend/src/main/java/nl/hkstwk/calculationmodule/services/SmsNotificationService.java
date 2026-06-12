package nl.hkstwk.calculationmodule.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("sms")
@Slf4j
public class SmsNotificationService implements NotificationService {
    public void send(String message, String recipient) {
        log.info("Sending SMS to {} with message: {}", recipient, message);
    }
}
