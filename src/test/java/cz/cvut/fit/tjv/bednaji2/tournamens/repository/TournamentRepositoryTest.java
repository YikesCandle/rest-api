package cz.cvut.fit.tjv.bednaji2.tournamens.repository;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Address;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest
class TournamentRepositoryTest {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PersonRepository personRepository;

    @Test
    @Transactional
    public void saveTournamentWithTwoAccounts_FindPersonTournaments() {
        Address address = Address.builder()
                .street("1989 Saint Francis Way")
                .city("Eagleville")
                .postalCode("19403")
                .phoneNumber("267-241-7349")
                .build();
        Person person = Person.builder()
                .name("James C Morris")
                .gender("male")
                .address(address)
                .dayOfBirth(LocalDate.parse("1975-05-20"))
                .build();

        Account account1 = Account.builder()
                .nick("HoltHamlet")
                .rank("gold")
                .email("raam.cztest1")
                .person(person)
                .build();
        Account account2 = Account.builder()
                .nick("HoltHamlet")
                .rank("challenger")
                .email("111znam.cztest2")
                .person(person)
                .build();
        accountRepository.save(account1);
        accountRepository.save(account2);
        Tournament tournament = Tournament.builder()
                .date(LocalDate.parse("1975-05-20"))
                .name("test tournament")
                .winner(person.getPersonId())
                .build();
        tournament.addAccount(account1);
        tournament.addAccount(account2);
        tournamentRepository.save(tournament);
        assertTrue(tournamentRepository.existsById(tournament.getTournamentId()));
        assertEquals(1, personRepository.findAllTournamentsOfPerson(person.getPersonId()).size());
    }
}