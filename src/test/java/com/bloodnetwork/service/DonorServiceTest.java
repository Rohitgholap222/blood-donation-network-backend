package com.bloodnetwork.service;

import com.bloodnetwork.dto.DonorResponseDto;
import com.bloodnetwork.entity.BloodGroup;
import com.bloodnetwork.entity.DonorProfile;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.repository.DonorProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DonorServiceTest {

    @Mock
    private DonorProfileRepository donorProfileRepository;

    @InjectMocks
    private DonorService donorService;

    @Test
    void testGetNearestDonors() {
        User user = new User();
        user.setName("John");
        user.setPhone("1234567890");

        DonorProfile profile = new DonorProfile();
        profile.setId(1L);
        profile.setUser(user);
        profile.setBloodGroup(BloodGroup.O_POS);
        profile.setLatitude(40.7128);
        profile.setLongitude(-74.0060);

        when(donorProfileRepository.findNearestDonors(org.mockito.ArgumentMatchers.anyList(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(Collections.singletonList(profile));

        List<DonorResponseDto> result = donorService.getNearestDonors("O_POS", 40.7128, -74.0060, 10);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("O_POS", result.get(0).getBloodGroup());
    }
}
