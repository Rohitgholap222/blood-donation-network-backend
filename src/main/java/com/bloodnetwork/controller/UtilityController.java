package com.bloodnetwork.controller;

import com.bloodnetwork.dto.ApiResponse;
import com.bloodnetwork.util.BloodGroupMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utils")
public class UtilityController {

    @GetMapping("/compatibility/{bloodGroup}")
    public ResponseEntity<ApiResponse<List<String>>> checkCompatibility(@PathVariable String bloodGroup) {
        try {
            List<String> compatible = BloodGroupMapper.getCompatibleGroups(bloodGroup);
            return ResponseEntity.ok(new ApiResponse<>(true, "Compatibility retrieved", compatible));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid blood group", null));
        }
    }

    @GetMapping("/blood-groups")
    public ResponseEntity<ApiResponse<Map<String, String>>> getBloodGroups() {
        Map<String, String> groups = Map.of(
            "A_POS", "A+",
            "A_NEG", "A-",
            "B_POS", "B+",
            "B_NEG", "B-",
            "AB_POS", "AB+",
            "AB_NEG", "AB-",
            "O_POS", "O+",
            "O_NEG", "O-"
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "Blood groups retrieved", groups));
    }
}
