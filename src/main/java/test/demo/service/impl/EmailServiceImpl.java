package test.demo.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.demo.dao.entity.EmailData;
import test.demo.dao.entity.PhoneData;
import test.demo.dao.entity.User;
import test.demo.dao.repo.EmailDataRepository;
import test.demo.dao.repo.PhoneDataRepository;
import test.demo.dao.repo.UserRepository;
import test.demo.exception.LogicException;
import test.demo.exception.NotFoundException;
import test.demo.service.EmailService;
import test.demo.service.JwtTokenUtil;
import test.demo.service.PhoneService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailDataRepository emailDataRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public void addEmail(String authorizationHeader, String email) {
        Long userId = jwtTokenUtil.extractUserId(authorizationHeader);

        if (emailDataRepository.findByEmail(email).isPresent()) {
            throw new LogicException("Email already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        EmailData newPhone = EmailData.builder()
                .user(user)
                .email(email)
                .build();

        emailDataRepository.save(newPhone);
    }

    @Override
    @Transactional
    public void deleteEmail(String authorizationHeader, String email) {
        Long userId = jwtTokenUtil.extractUserId(authorizationHeader);

        EmailData phoneData = emailDataRepository.findByEmail(email)
                .filter(p -> p.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException("Email not found or does not belong to user"));

        int phoneCount = emailDataRepository.findByUser(phoneData.getUser()).size();
        if (phoneCount <= 1) {
            throw new LogicException("User must have at least one phone");
        }

        emailDataRepository.delete(phoneData);
    }
}