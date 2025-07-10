package test.demo.service;

public interface PhoneService {
    void addPhone(String authorizationHeader, String phone);
    void deletePhone(String authorizationHeader, String phone);
}
