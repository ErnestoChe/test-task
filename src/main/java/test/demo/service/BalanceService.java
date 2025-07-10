package test.demo.service;

import java.math.BigDecimal;

public interface BalanceService {
    void increaseBalancesPeriodically();
    void transfer(String authorization, Long recipientUserId, BigDecimal amount);

}
