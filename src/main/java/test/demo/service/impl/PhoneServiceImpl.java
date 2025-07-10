package test.demo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.demo.dao.entity.PhoneData;
import test.demo.dao.entity.User;
import test.demo.dao.repo.PhoneDataRepository;
import test.demo.dao.repo.UserRepository;
import test.demo.service.JwtTokenUtil;
import test.demo.service.PhoneService;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneDataRepository phoneDataRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public void addPhone(String authorizationHeader, String phone) {
        Long userId = jwtTokenUtil.extractUserId(authorizationHeader);

        if (phoneDataRepository.findByPhone(phone).isPresent()) {
            throw new RuntimeException("Phone already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PhoneData newPhone = PhoneData.builder()
                .user(user)
                .phone(phone)
                .build();

        phoneDataRepository.save(newPhone);
    }

    @Override
    @Transactional
    public void deletePhone(String authorizationHeader, String phone) {
        Long userId = jwtTokenUtil.extractUserId(authorizationHeader);

        PhoneData phoneData = phoneDataRepository.findByPhone(phone)
                .filter(p -> p.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Phone not found or does not belong to user"));

        int phoneCount = phoneDataRepository.findByUser(phoneData.getUser()).size();
        if (phoneCount <= 1) {
            throw new RuntimeException("User must have at least one phone");
        }

        phoneDataRepository.delete(phoneData);
    }
}