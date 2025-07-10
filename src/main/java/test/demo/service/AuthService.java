package test.demo.service;

import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;

public interface AuthService {
    LoginResponse authenticate(LoginRequest request);
}