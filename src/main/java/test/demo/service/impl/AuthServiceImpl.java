package test.demo.service.impl;

import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.demo.dao.entity.User;
import test.demo.dao.repo.UserRepository;
import test.demo.service.AuthService;
import test.demo.service.JwtTokenUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        Optional<User> userOpt;

        if (request.getEmail() != null) {
            userOpt = userRepository.findByEmails_Email(request.getEmail());
        } else if (request.getPhone() != null) {
            userOpt = userRepository.findByPhones_Phone(request.getPhone());
        } else {
            throw new IllegalArgumentException("Email or phone must be provided");
        }

        //TODO user not found
        User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtTokenUtil.generateToken(user.getId());

        return LoginResponse.builder().token(token).build();
    }
}