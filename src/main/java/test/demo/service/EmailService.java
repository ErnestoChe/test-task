package test.demo.service;

public interface EmailService {
    void addEmail(String authorizationHeader, String email);
    void deleteEmail(String authorizationHeader, String email);
}
