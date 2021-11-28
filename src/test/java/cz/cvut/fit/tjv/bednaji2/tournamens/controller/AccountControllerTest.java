package cz.cvut.fit.tjv.bednaji2.tournamens.controller;

import cz.cvut.fit.tjv.bednaji2.tournamens.business.AccountService;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Address;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    Address address;
    Person person1;
    Account account1;
    Account account2;
    Account nonExistingAccount;
    Tournament tournament1;


    @BeforeEach
    public void setup() {
        address = Address.builder()
                .street("1989 Saint Francis Way")
                .city("Eagleville")
                .postalCode("19403")
                .phoneNumber("267-241-7349")
                .build();
        person1 = Person.builder()
                .personId(1L)
                .name("TestPerson1")
                .gender("male")
                .address(address)
                .dayOfBirth(LocalDate.parse("1975-05-20"))
                .build();

        account1 = Account.builder()
                .accountId(1L)
                .nick("testAcocuontNick1")
                .rank("testRank1")
                .email("test@email.1")
                .person(person1)
                .build();
        account2 = Account.builder()
                .accountId(2L)
                .nick("testAcocuontNick2")
                .rank("testRank2")
                .email("test@email.2")
                .person(person1)
                .build();
        tournament1 = Tournament.builder()
                .date(LocalDate.parse("1975-05-20"))
                .name("test tournament")
                .winner(person1.getPersonId())
                .build();
        tournament1.addAccount(account1);
        tournament1.addAccount(account2);
        nonExistingAccount = Account.builder().accountId(3L).build();
    }

    @Test
    public void findAllAccountsTest() throws Exception {
        List<Account> accounts = List.of(account1, account2);

        Mockito.when(accountService.findAll()).thenReturn(accounts);
        mockMvc.perform(get("/account")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nick").value(account1.getNick()))
                .andExpect(jsonPath("$[1].accountId").value(account2.getAccountId()));

    }
    @Test
    public void findAccountTest() throws Exception {

        Mockito.when(accountService.findById(account1.getAccountId())).thenReturn(account1);
        Mockito.when(accountService.findById(account2.getAccountId())).thenReturn(account2);
        Mockito.when(accountService.findById(nonExistingAccount.getAccountId())).thenThrow(
                new EntityNotFoundException());
        mockMvc.perform(get("/account/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick").value(account1.getNick()))
                .andExpect(jsonPath("$.accountId").value(account1.getAccountId()))
                .andExpect(jsonPath("$.person.name").value(person1.getName()));

        mockMvc.perform(get("/account/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick").value(account2.getNick()))
                .andExpect(jsonPath("$.accountId").value(account2.getAccountId()))
                .andExpect(jsonPath("$.person.name").value(person1.getName()));

        mockMvc.perform(get("/account/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}