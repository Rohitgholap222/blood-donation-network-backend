package com.bloodnetwork.service;

import com.bloodnetwork.dto.AuthRequest;
import com.bloodnetwork.dto.AuthResponse;
import com.bloodnetwork.dto.RegisterRequest;
import com.bloodnetwork.entity.DonorProfile;
import com.bloodnetwork.entity.Role;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.exception.BadRequestException;
import com.bloodnetwork.repository.DonorProfileRepository;
import com.bloodnetwork.repository.UserRepository;
import com.bloodnetwork.security.CustomUserDetails;
import com.bloodnetwork.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DonorProfileRepository donorProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole() != null ? request.getRole() : Role.DONOR)
                .build();

        user = userRepository.save(user);

        if (user.getRole() == Role.DONOR) {
            if (request.getBloodGroup() == null) {
                throw new BadRequestException("Blood group is required for donors");
            }
            DonorProfile profile = DonorProfile.builder()
                    .user(user)
                    .bloodGroup(request.getBloodGroup())
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .isAvailable(true)
                    .build();
            donorProfileRepository.save(profile);
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken, user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken, user.getEmail(), user.getRole().name());
    }
}
