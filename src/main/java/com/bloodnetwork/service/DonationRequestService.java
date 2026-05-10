package com.bloodnetwork.service;

import com.bloodnetwork.dto.DonationRequestCreatedEvent;
import com.bloodnetwork.dto.DonationRequestDto;
import com.bloodnetwork.entity.DonationRequest;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.repository.DonationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DonationRequestService {

    private final DonationRequestRepository donationRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public DonationRequest createRequest(DonationRequestDto dto, User hospital) {
        DonationRequest request = DonationRequest.builder()
                .hospital(hospital)
                .bloodGroup(dto.getBloodGroup())
                .unitsRequired(dto.getUnitsRequired())
                .urgency(dto.getUrgency())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .status("PENDING")
                .build();

        request = donationRequestRepository.save(request);

        eventPublisher.publishEvent(new DonationRequestCreatedEvent(
                request.getId(),
                request.getBloodGroup().name(),
                request.getLatitude(),
                request.getLongitude(),
                hospital.getName()
        ));

        return request;
    }

    @Transactional
    public void fulfillRequest(Long requestId, User donor) {
        DonationRequest request = donationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Request is not pending");
        }
        
        request.setStatus("FULFILLED");
        request.setDonor(donor);
        donationRequestRepository.save(request);
    }

    public java.util.List<DonationRequest> getRequestsByHospital(Long hospitalId) {
        return donationRequestRepository.findByHospitalId(hospitalId);
    }

    public java.util.List<DonationRequest> getRequestsByDonor(Long donorId) {
        return donationRequestRepository.findByDonorId(donorId);
    }

    public java.util.List<DonationRequest> getAllPendingRequests() {
        return donationRequestRepository.findByStatus("PENDING");
    }
}
