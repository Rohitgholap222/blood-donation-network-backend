package com.bloodnetwork.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BloodGroup {
    A_POS("A+"), 
    A_NEG("A-"), 
    B_POS("B+"), 
    B_NEG("B-"), 
    AB_POS("AB+"), 
    AB_NEG("AB-"), 
    O_POS("O+"), 
    O_NEG("O-");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static BloodGroup fromString(String text) {
        for (BloodGroup b : BloodGroup.values()) {
            if (b.value.equalsIgnoreCase(text) || b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unknown blood group: " + text);
    }
}
