package com.example.enterprise.service.impl;

import com.example.enterprise.dto.JwtResponse;
import com.example.enterprise.dto.LoginRequest;
import com.example.enterprise.dto.RegisterRequest;
import com.example.enterprise.entity.Role;
import com.example.enterprise.entity.User;
import com.example.enterprise.exception.BadRequestException;
import com.example.enterprise.repository.RoleRepository;
import com.example.enterprise.repository.UserRepository;
import com.example.enterprise.security.CustomUserDetails;
import com.example.enterprise.security.JwtTokenProvider;
import com.example.enterprise.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BadRequestException("Default USER role not configured"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);

        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);
        return new JwtResponse(token, "Bearer");
    }
}

