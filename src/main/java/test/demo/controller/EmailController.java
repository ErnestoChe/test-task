package test.demo.controller;

import com.example.api.EmailControllerApi;
import com.example.dto.EmailInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController implements EmailControllerApi {

    @Override
    public ResponseEntity<Void> userEmailsEmailDelete(String authorization, String email) {
        return null;
    }

    @Override
    public ResponseEntity<Void> userEmailsPost(String authorization, EmailInput emailInput) {
        return null;
    }
}
