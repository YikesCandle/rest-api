package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import cz.cvut.fit.tjv.bednaji2.tournamens.error.EntityStateException;
import cz.cvut.fit.tjv.bednaji2.tournamens.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PersonServiceTest {

    @Autowired
    PersonService personService;

    @MockBean
    PersonRepository personRepository;
    @MockBean
    AccountService accountService;

    Person nonExistingPerson;
    Person existingPerson;
    Account validAccount;
    Account nonExistingAccount;

    @BeforeEach
    public void setup() {
        nonExistingPerson = Person.builder().personId(1L).build();
        existingPerson = Person.builder().personId(2L).build();
        validAccount = Account.builder().accountId(1L).build();
        nonExistingAccount = Account.builder().accountId(2L).build();

        Mockito.when(personRepository.existsById(nonExistingPerson.getPersonId())).thenReturn(false);
        Mockito.when(personRepository.existsById(existingPerson.getPersonId())).thenReturn(true);
        Mockito.when(accountService.createAccount(validAccount)).thenReturn(validAccount);
        Mockito.when(personRepository.save(nonExistingPerson)).thenReturn(nonExistingPerson);
        Mockito.when(personRepository.save(existingPerson)).thenReturn(existingPerson);
        Mockito.when(personRepository.findById(existingPerson.getPersonId()))
                .thenReturn(Optional.of(existingPerson));
        Mockito.when(personRepository.findById(nonExistingPerson.getPersonId()))
                .thenReturn(Optional.empty());
    }

    @Test
    public void updatePersonTest() {
        assertThrows(EntityStateException.class,
                () -> personService.updatePerson(nonExistingPerson.getPersonId(), nonExistingPerson));
        personService.updatePerson(existingPerson.getPersonId(), existingPerson);
    }
    @Test
    public void createNewAccountTest() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.createNewAccount(nonExistingPerson.getPersonId(), validAccount));
        personService.createNewAccount(existingPerson.getPersonId(),validAccount);
    }
    @Test
    public void updateAccountTest() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.updateAccount(
                        nonExistingPerson.getPersonId(), validAccount.getAccountId(), validAccount));
        //Invalid account are properly throwing (tested in accounts)
        personService.updateAccount(existingPerson.getPersonId(), validAccount.getAccountId(), validAccount);
    }
    @Test
    public void deleteAccountTest() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.deleteAccount(nonExistingPerson.getPersonId(), validAccount.getAccountId()));
        assertThrows(ResponseStatusException.class,
                () -> personService.deleteAccount(existingPerson.getPersonId(), validAccount.getAccountId()));
    }
    @Test
    public void getAccountTest() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.getAccount(nonExistingPerson.getPersonId(), validAccount.getAccountId()));
        personService.getAccount(existingPerson.getPersonId(), validAccount.getAccountId());
    }
    @Test
    public void getAllAccountsTest() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.getAllAccounts(nonExistingPerson.getPersonId()));
        personService.getAllAccounts(existingPerson.getPersonId());
    }
}