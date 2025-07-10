package test.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import test.demo.dao.entity.Account;
import test.demo.dao.repo.AccountRepository;
import test.demo.exception.BalanceException;
import test.demo.service.BalanceService;
import test.demo.service.JwtTokenUtil;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

	@Autowired
	private BalanceService balanceService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@MockitoBean
	private AccountRepository accountRepository;

	Long PAYER = 1l;
	Long RECEIVER = 2l;

	@Test
	void smokeTest() {
		log.info("Context loads");
	}

	@Test
	void balanceTransfers() {

		BigDecimal amount = new BigDecimal(5l);
		String token = "Bearer " + jwtTokenUtil.generateToken(1l);
		Account payerAcc = createAccount(PAYER, BigDecimal.TEN);
		Account recipientAcc = createAccount(RECEIVER, BigDecimal.TEN);
		Mockito.when(accountRepository.findByUserId(PAYER)).thenReturn(Optional.ofNullable(payerAcc));
		Mockito.when(accountRepository.findByUserId(RECEIVER)).thenReturn(Optional.ofNullable(recipientAcc));

		balanceService.transfer(token, RECEIVER, amount);

		assertEquals(payerAcc.getBalance(), BigDecimal.valueOf(5l));
		assertEquals(recipientAcc.getBalance(), BigDecimal.valueOf(15l));
	}

	@Test
	void insufficientBalance() {

		BigDecimal amount = new BigDecimal(5l);
		String token = "Bearer " + jwtTokenUtil.generateToken(1l);
		Account payerAcc = createAccount(PAYER, BigDecimal.ZERO);
		Account recipientAcc = createAccount(RECEIVER, BigDecimal.TEN);
		Mockito.when(accountRepository.findByUserId(PAYER)).thenReturn(Optional.ofNullable(payerAcc));
		Mockito.when(accountRepository.findByUserId(RECEIVER)).thenReturn(Optional.ofNullable(recipientAcc));

		assertThrows(BalanceException.class, ()-> balanceService.transfer(token, RECEIVER, amount));
	}

	private Account createAccount(Long userId, BigDecimal amount) {
		return Account.builder()
				.id(userId)
				.balance(amount)
				.build();
	}

}
