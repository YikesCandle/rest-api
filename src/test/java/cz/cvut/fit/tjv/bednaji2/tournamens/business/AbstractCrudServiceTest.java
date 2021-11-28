package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import cz.cvut.fit.tjv.bednaji2.tournamens.error.EntityStateException;
import cz.cvut.fit.tjv.bednaji2.tournamens.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class AbstractCrudServiceTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    private final Person existingPerson = Person.builder().name("testPerson1").personId(1L).build();
    private final Person nonExistingPerson = Person.builder().name("testPerson2").personId(2L).build();

    @BeforeEach
    void setUp() {
        Mockito.when(personRepository.existsById(existingPerson.getPersonId())).thenReturn(true);
        Mockito.when(personRepository.existsById(nonExistingPerson.getPersonId())).thenReturn(false);
        Mockito.when(personRepository.findById(existingPerson.getPersonId())).thenReturn(Optional.of(existingPerson));
        Mockito.when(personRepository.findById(nonExistingPerson.getPersonId())).thenReturn(Optional.empty());
        Mockito.when(personRepository.findAll()).thenReturn(List.of(existingPerson));
        Mockito.when(personRepository.save(existingPerson)).thenReturn(existingPerson);
        Mockito.when(personRepository.save(nonExistingPerson)).thenReturn(nonExistingPerson);
    }

    @Test
    void findById() {
        Assertions.assertEquals(existingPerson, personService.findById(existingPerson.getPersonId()));
        Mockito.verify(personRepository, Mockito.atLeast(1)).findById(existingPerson.getPersonId());
    }

    @Test
    void readAll() {
        var retCol = List.copyOf(personService.findAll());
        var exp = List.of(existingPerson);
        Assertions.assertEquals(exp, retCol);
        Mockito.verify(personRepository, Mockito.atLeast(1)).findAll();
    }

    @Test
    void update() {
        Assertions.assertThrows(EntityStateException.class, () -> personService.update(nonExistingPerson));
        Mockito.verify(personRepository, Mockito.never()).save(nonExistingPerson);

        personService.update(existingPerson);
        Mockito.verify(personRepository, Mockito.atLeast(1)).save(existingPerson);
    }

    @Test
    void deleteById() {
        personService.deleteById(existingPerson.getPersonId());
        Mockito.verify(personRepository, Mockito.atLeast(1)).deleteById(existingPerson.getPersonId());
    }
}