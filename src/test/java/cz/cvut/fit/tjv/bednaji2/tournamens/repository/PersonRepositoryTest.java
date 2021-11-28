package cz.cvut.fit.tjv.bednaji2.tournamens.repository;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Address;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;
    final private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    public void savePerson() {
        Address address = Address.builder()
                .street("3312 University Hill Road")
                .city("Champaign")
                .postalCode("61820")
                .phoneNumber("217-531-8556")
                .build();
        Person person = Person.builder()
                .name("Beverley J McLaughlin")
                .gender("female")
                .address(address)
                .dayOfBirth(LocalDate.parse("1963-07-28", formatter))
                .build();
        personRepository.save(person);
        assertTrue(personRepository.existsById(person.getPersonId()));
    }

    @Test
    public void savePersonWithoutAddress() {
        Person person = Person.builder()
                .name("Scott E Stenson")
                .gender("male")
                .dayOfBirth(LocalDate.parse("1982-08-02", formatter))
                .build();
        personRepository.save(person);
        assertTrue(personRepository.existsById(person.getPersonId()));
    }
}