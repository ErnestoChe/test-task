package test.demo.controller;

import com.example.api.EmailControllerApi;
import com.example.dto.EmailInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import test.demo.service.EmailService;

@RestController
@RequiredArgsConstructor
public class EmailController implements EmailControllerApi {

    private final EmailService emailService;

    @Override
    public ResponseEntity<Void> userEmailsEmailDelete(String authorization, String email) {
        emailService.deleteEmail(authorization, email);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> userEmailsPost(String authorization, EmailInput emailInput) {
        emailService.addEmail(authorization, emailInput.getEmail());
        return ResponseEntity.noContent().build();
    }
}
