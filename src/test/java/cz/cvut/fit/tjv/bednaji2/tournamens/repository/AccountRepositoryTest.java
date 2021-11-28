package cz.cvut.fit.tjv.bednaji2.tournamens.repository;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Address;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Test elementary repository functions")
    public void saveAccountWithOwner_andFoundByEmail_andDelete() {
        Address address = Address.builder()
                .street("3879 Williams Mine Road")
                .city("UNIVERSITY")
                .postalCode("38677")
                .phoneNumber("908-316-7946")
                .build();
        Person person = Person.builder()
                .name("Jennifer J Schlemmer")
                .gender("female")
                .address(address)
                .dayOfBirth(LocalDate.parse("1998-02-21"))
                .build();
        Account account = Account.builder()
                .nick("GilFrog")
                .rank("silver")
                .email("testtest@gmail.com")
                .person(person)
                .build();
        accountRepository.deleteAll();
        accountRepository.save(account);
        assertTrue(accountRepository.existsByEmail("testtest@gmail.com"));
        Iterable<Account> accounts = accountRepository.findAll();
        Assertions.assertThat(accounts).extracting(Account::getEmail).containsOnly("testtest@gmail.com");
        assertTrue(accountRepository.existsByEmail("testtest@gmail.com"));
        accountRepository.delete(account);
        Assertions.assertThat(accountRepository.findAll()).isEmpty();
    }
}