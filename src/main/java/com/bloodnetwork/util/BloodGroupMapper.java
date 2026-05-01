package com.bloodnetwork.util;

import com.bloodnetwork.entity.BloodGroup;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BloodGroupMapper {

    private static final Map<BloodGroup, List<BloodGroup>> COMPATIBILITY_MAP = Map.of(
            BloodGroup.A_POS, Arrays.asList(BloodGroup.A_POS, BloodGroup.A_NEG, BloodGroup.O_POS, BloodGroup.O_NEG),
            BloodGroup.A_NEG, Arrays.asList(BloodGroup.A_NEG, BloodGroup.O_NEG),
            BloodGroup.B_POS, Arrays.asList(BloodGroup.B_POS, BloodGroup.B_NEG, BloodGroup.O_POS, BloodGroup.O_NEG),
            BloodGroup.B_NEG, Arrays.asList(BloodGroup.B_NEG, BloodGroup.O_NEG),
            BloodGroup.AB_POS, Arrays.asList(BloodGroup.values()),
            BloodGroup.AB_NEG, Arrays.asList(BloodGroup.AB_NEG, BloodGroup.A_NEG, BloodGroup.B_NEG, BloodGroup.O_NEG),
            BloodGroup.O_POS, Arrays.asList(BloodGroup.O_POS, BloodGroup.O_NEG),
            BloodGroup.O_NEG, Arrays.asList(BloodGroup.O_NEG)
    );

    public static List<String> getCompatibleGroups(String requestedGroup) {
        BloodGroup group = BloodGroup.valueOf(requestedGroup);
        return COMPATIBILITY_MAP.get(group).stream()
                .map(Enum::name)
                .toList();
    }
}
