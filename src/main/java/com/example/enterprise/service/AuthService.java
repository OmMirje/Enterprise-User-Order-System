package com.example.enterprise.service;

import com.example.enterprise.dto.JwtResponse;
import com.example.enterprise.dto.LoginRequest;
import com.example.enterprise.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    JwtResponse login(LoginRequest request);
}

