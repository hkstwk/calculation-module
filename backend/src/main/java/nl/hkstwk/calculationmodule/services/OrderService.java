package nl.hkstwk.calculationmodule.services;

import lombok.RequiredArgsConstructor;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final NotificationFactory notificationFactory;

    public void completeOrder(CompoundInterestResponseDto dto, String notificationType) {
        notificationFactory
                .getService(notificationType)
                .send(dto.getFinalAmount().toString(), "jooooohhhh");
    }
}