package com.bloodnetwork.service;

import com.bloodnetwork.dto.DonationRequestCreatedEvent;
import com.bloodnetwork.dto.DonorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final DonorService donorService;

    @Async
    @EventListener
    public void handleDonationRequestCreatedEvent(DonationRequestCreatedEvent event) {
        log.info("Received DonationRequestCreatedEvent for Request ID: {}", event.getRequestId());
        
        List<DonorResponseDto> nearestDonors = donorService.getNearestDonors(
                event.getBloodGroup(), event.getLatitude(), event.getLongitude(), 10);
                
        for (DonorResponseDto donor : nearestDonors) {
            String notificationMessage = String.format("Urgent! %s blood required at %s", 
                    event.getBloodGroup(), event.getHospitalName());
                    
            messagingTemplate.convertAndSendToUser(
                    donor.getId().toString(),
                    "/queue/notifications",
                    notificationMessage
            );
            log.info("Notified Donor ID: {} about Request ID: {}", donor.getId(), event.getRequestId());
        }
    }
}
