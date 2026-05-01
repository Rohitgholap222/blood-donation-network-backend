package com.bloodnetwork.repository;

import com.bloodnetwork.entity.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {
    List<DonationRequest> findByHospitalId(Long hospitalId);
}
