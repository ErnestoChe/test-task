package test.demo.controller;

import com.example.api.PhoneControllerApi;
import com.example.dto.PhoneInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import test.demo.service.PhoneService;

@RestController
@RequiredArgsConstructor
public class PhoneController implements PhoneControllerApi {

    private final PhoneService phoneService;

    @Override
    public ResponseEntity<Void> userPhonesPhoneDelete(String authorization, String phone) {
        phoneService.deletePhone(authorization, phone);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> userPhonesPost(String authorization, PhoneInput phoneInput) {
        phoneService.addPhone(authorization, phoneInput.getPhone());
        return ResponseEntity.noContent().build();
    }
}
