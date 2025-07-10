package test.demo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import test.demo.dao.entity.Account;
import test.demo.dao.repo.AccountRepository;
import test.demo.exception.BalanceException;
import test.demo.exception.LogicException;
import test.demo.exception.NotFoundException;
import test.demo.service.BalanceService;
import test.demo.service.JwtTokenUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceServiceImpl implements BalanceService {
    private final AccountRepository accountRepository;
    private final JwtTokenUtil jwtTokenUtil;

    private static final BigDecimal MAX_AMOUNT_AFTER_INCREASE = new BigDecimal("207");
    private static final BigDecimal INCREASE_PERCENT = new BigDecimal("0.10");

    @Override
    @Transactional
    @Scheduled(fixedRate = 30_000)
    public void increaseBalancesPeriodically() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            BigDecimal current = account.getBalance();
            BigDecimal increase = current.multiply(INCREASE_PERCENT).setScale(2, RoundingMode.HALF_UP);
            if (current.compareTo(MAX_AMOUNT_AFTER_INCREASE) < 0) {
                account.setBalance(account.getBalance().add(increase));
                log.info("Increased account {} by {}", account.getId(), increase);
            } else {
                log.info("Account is too big for auto increase {}", account.getId());
            }
        }

        accountRepository.saveAll(accounts);
        log.info("Balances increased by 10% (up to 207%)");
    }

    @Override
    @Transactional
    public void transfer(String authorization, Long recipientUserId, BigDecimal amount) {
        Long senderUserId = jwtTokenUtil.extractUserId(authorization);

        if (senderUserId.equals(recipientUserId)) {
            throw new LogicException("Нельзя переводить самому себе");
        }

        Account senderAccount = accountRepository.findByUserId(senderUserId)
                .orElseThrow(() -> new NotFoundException("Аккаунт отправителя не найден"));
        Account recipientAccount = accountRepository.findByUserId(recipientUserId)
                .orElseThrow(() -> new NotFoundException("Аккаунт получателя не найден"));

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new BalanceException("Недостаточно средств для перевода");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        log.info("Transferred {} from user {} to user {}", amount, senderUserId, recipientUserId);
    }
}
