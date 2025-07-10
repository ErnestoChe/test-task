package test.demo.controller;


import com.example.api.AuthApi;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import test.demo.service.AuthService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<LoginResponse> authTokenPost(@Valid LoginRequest loginRequest) {
        LoginResponse authenticate = authService.authenticate(loginRequest);
        log.info("successful login for email: {} or phone: {}", loginRequest.getEmail(), loginRequest.getPhone());
        return ResponseEntity.ok(authenticate);
    }
}
