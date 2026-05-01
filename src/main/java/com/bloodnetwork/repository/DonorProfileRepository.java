package com.bloodnetwork.repository;

import com.bloodnetwork.entity.DonorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface DonorProfileRepository extends JpaRepository<DonorProfile, Long> {
    Optional<DonorProfile> findByUserId(Long userId);

    @Query(value = "SELECT dp.* FROM donor_profiles dp " +
            "WHERE dp.blood_group IN :bloodGroups AND dp.is_available = true " +
            "AND (dp.last_donation_date IS NULL OR DATEDIFF(CURDATE(), dp.last_donation_date) >= 90) " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(dp.latitude)) * " +
            "cos(radians(dp.longitude) - radians(:lng)) + sin(radians(:lat)) * " +
            "sin(radians(dp.latitude)))) ASC LIMIT :limit", nativeQuery = true)
    List<DonorProfile> findNearestDonors(
            @Param("bloodGroups") List<String> bloodGroups,
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("limit") int limit);
}
