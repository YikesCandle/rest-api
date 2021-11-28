package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import cz.cvut.fit.tjv.bednaji2.tournamens.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService extends AbstractCrudService<Person, Long, PersonRepository> {

    private final AccountService accountService;
    private final TournamentService tournamentService;

    @Autowired
    public PersonService(PersonRepository personRepository,
                         AccountService accountService,
                         TournamentService tournamentService) {
        super(personRepository);
        this.accountService = accountService;
        this.tournamentService = tournamentService;
    }

    @Override
    protected boolean exists(Person person) {
        return repository.existsById(person.getPersonId());
    }

    public void updatePerson(Long id, Person person) {
        person.setPersonId(id);
        update(person);
    }

    public Person createPerson(Person person) {
        return repository.save(person);
    }

    public Account createNewAccount(Long personId, Account account) {
        if (!existsById(personId))
            throw new EntityNotFoundException("Person not found");
        account.setPerson(findById(personId));
        return accountService.createAccount(account);
    }

    public void updateAccount(Long personId, Long accountId, Account account) {
        if (!existsById(personId))
            throw new EntityNotFoundException("Person not found");
        account.setPerson(findById(personId));
        accountService.updateAccount(accountId, account);
    }

    public void deleteAccount(Long personId, Long accountId) {
        if (!existsById(personId))
            throw new EntityNotFoundException("Person not found");
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        //accountService.deleteById(accountId);
    }

    public Account getAccount(Long personId, Long accountId) {
        if (!existsById(personId))
            throw new EntityNotFoundException("Person not found.");
        return accountService.findById(accountId);
    }

    public List<Account> getAllAccounts(Long personId) {
        if (!existsById(personId))
            throw new EntityNotFoundException("Person not found.");
        return accountService.findAllAccountsOfPerson(personId);
    }

    public void deletePersonById(Long id) {
        for (Account account : accountService.findAllAccountsOfPerson(id)) {
            account.setPerson(null);
            accountService.update(account);
        }
        repository.deleteById(id);
    }

    public List<Tournament> findAllTournaments(Long personId) {
        if (!existsById(personId))
            throw new EntityNotFoundException("Person not found.");
        return tournamentService.getAllByIds(
                repository.findAllTournamentsOfPerson(personId));
    }
}
