package test.demo.controller;

import com.example.api.BalanceControllerApi;
import com.example.dto.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import test.demo.service.BalanceService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Service
public class BalanceController implements BalanceControllerApi {

    private final BalanceService balanceService;

    @Override
    public ResponseEntity<Void> balanceTransferPost(String authorization, TransferRequest transferRequest) {
        balanceService.transfer(authorization, transferRequest.getReceiver(), BigDecimal.valueOf(transferRequest.getAmount()));
        return ResponseEntity.ok().build();
    }
}
