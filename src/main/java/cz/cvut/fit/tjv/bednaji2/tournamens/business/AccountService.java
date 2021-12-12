package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AccountService extends AbstractCrudService<Account, Long, AccountRepository> {

    public AccountService(AccountRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Account account) {
        return repository.existsById(account.getAccountId());
    }

    public void updateAccount(Long accountId, Account account) {
        Account original = findById(accountId);
        if (!original.getEmail().equalsIgnoreCase(account.getEmail()) // email is changed
            && repository.existsByEmail(account.getEmail()))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "email already exists");
        account.setAccountId(accountId);
        update(account);
    }

    public List<Account> findAllAccountsOfPerson(Long personId) {
        return repository.findAllByPerson_PersonId(personId);
    }

    public Account createAccount(Account account) {
        if (repository.existsByEmail(account.getEmail()))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "email already exists");
        return repository.save(account);
    }

    public List<Account> findAllByTournamentId(Long id) {
        return repository.findAllByTournaments_tournamentId(id);
    }
}
