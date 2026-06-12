package nl.hkstwk.calculationmodule.services;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationFactory {

    private final Map<String, NotificationService> services;

    public NotificationFactory(Map<String, NotificationService> services) {
        this.services = services;
    }

    public NotificationService getService(String type) {
        NotificationService service = services.get(type.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Unknown notification type: " + type);
        }
        return service;
    }
}
