package test.demo.service;

public interface EmailService {
    void addPhone(String authorizationHeader, String email);
    void deletePhone(String authorizationHeader, String email);
}
