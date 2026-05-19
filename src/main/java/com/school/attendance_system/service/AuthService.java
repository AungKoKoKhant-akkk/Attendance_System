package com.school.attendance_system.service;

import com.school.attendance_system.dto.request.RegisterRequest;
import com.school.attendance_system.dto.response.AuthResponse;
import com.school.attendance_system.dto.response.LoginRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
