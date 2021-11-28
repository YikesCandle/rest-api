package cz.cvut.fit.tjv.bednaji2.tournamens.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.bednaji2.tournamens.business.AccountService;
import cz.cvut.fit.tjv.bednaji2.tournamens.business.PersonService;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "person")
public class PersonController {

    PersonService personService;
    AccountService accountService;

    @Autowired
    public PersonController(PersonService personService,
                            AccountService accountService) {
        this.personService = personService;
        this.accountService = accountService;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Person createPerson(@Valid @RequestBody Person person) {
        return personService.createPerson(person);
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Person> getAllPersons() {
        return personService.findAll();
    }

    @GetMapping(path = "{personId}")
    public Person getPerson(@PathVariable("personId") Long id) {
        return personService.findById(id);
    }

    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping(path = "{personId}")
    public void updatePerson(@PathVariable("personId") Long id, @Valid @RequestBody Person person) {
        personService.updatePerson(id, person);
    }

    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @DeleteMapping(path = "{personId}")
    public void deletePerson(@PathVariable("personId") Long id) {
        personService.deletePersonById(id);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "{personId}/account")
    public Account createNewAccount(@PathVariable("personId") Long id,
                                    @Valid @RequestBody Account account) {
        return personService.createNewAccount(id, account);
    }

    @GetMapping(path = "{personId}/account/{accountId}")
    public Account findAccount(@PathVariable("personId") Long personId,
                                 @PathVariable("accountId") Long accountId) {
        return personService.getAccount(personId, accountId);
    }

    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping(path = "{personId}/account/{accountId}")
    public void updateAccount(@PathVariable("personId") Long personId,
                                 @PathVariable("accountId") Long accountId,
                                 @Valid @RequestBody Account account) {
        personService.updateAccount(personId, accountId, account);
    }

    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @DeleteMapping(path = "{personId}/account/{accountId}")
    public void deleteAccount(@PathVariable("personId") Long personId,
                              @PathVariable("accountId") Long accountId) {
        personService.deleteAccount(personId, accountId);
    }
    @JsonView(Views.SummaryNoPerson.class)
    @GetMapping(path = "{personId}/account")
    public List<Account> findAllAccounts(@PathVariable("personId") Long personId) {
        return personService.getAllAccounts(personId);
    }

    @GetMapping(path = "{personId}/tournament")
    public List<Tournament> findAllTournaments(@PathVariable("personId") Long personId) {
        return personService.findAllTournaments(personId);
    }
}
