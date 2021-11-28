package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @MockBean
    AccountRepository accountRepository;

    Account accountWithUniqueEmail;
    Account accountWithoutUniqueEmail;
    Account nonExistingAccount;

    @BeforeEach
    public void setup() {
        accountWithUniqueEmail = Account.builder().accountId(1L).email("nonExistingEmail").build();
        accountWithoutUniqueEmail = Account.builder().accountId(2L).email("existingEmail").build();
        nonExistingAccount = Account.builder().accountId(3L).build();

        Mockito.when(accountRepository.existsByEmail("nonExistingEmail")).thenReturn(false);
        Mockito.when(accountRepository.existsByEmail("existingEmail")).thenReturn(true);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(accountWithUniqueEmail));
        Mockito.when(accountRepository.save(accountWithUniqueEmail)).thenReturn(accountWithUniqueEmail);
        Mockito.when(accountRepository.existsById(accountWithUniqueEmail.getAccountId())).thenReturn(true);
    }

    @Test
    public void updateAccountTest() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> accountService.updateAccount(nonExistingAccount.getAccountId(), accountWithoutUniqueEmail));
        Assertions.assertThrows(ResponseStatusException.class,
                () -> accountService.updateAccount(accountWithUniqueEmail.getAccountId(), accountWithoutUniqueEmail));
        accountService.updateAccount(accountWithUniqueEmail.getAccountId(), accountWithUniqueEmail);
    }

    @Test
    public void createAccountTest() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> accountService.createAccount(accountWithoutUniqueEmail));
        Assertions.assertEquals(accountWithUniqueEmail, accountService.createAccount(accountWithUniqueEmail));
    }
}