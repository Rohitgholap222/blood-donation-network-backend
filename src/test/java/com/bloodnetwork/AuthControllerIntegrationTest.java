package com.bloodnetwork;

import com.bloodnetwork.dto.AuthRequest;
import com.bloodnetwork.dto.RegisterRequest;
import com.bloodnetwork.entity.BloodGroup;
import com.bloodnetwork.entity.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterAndLogin() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Test Donor");
        registerRequest.setEmail("testdonor@example.com");
        registerRequest.setPassword("password");
        registerRequest.setPhone("9876543210");
        registerRequest.setRole(Role.DONOR);
        registerRequest.setBloodGroup(BloodGroup.A_POS);
        registerRequest.setLatitude(12.9716);
        registerRequest.setLongitude(77.5946);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists());

        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("testdonor@example.com");
        authRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists());
    }
}
