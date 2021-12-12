package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.NewTournament;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import cz.cvut.fit.tjv.bednaji2.tournamens.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TournamentService extends AbstractCrudService<Tournament, Long, TournamentRepository> {

    private final AccountService accountService;

    @Autowired
    public TournamentService(TournamentRepository repository, AccountService accountService) {
        super(repository);
        this.accountService = accountService;
    }

    public List<Tournament> getAllByIds(List<Long> allTournamentsOfPerson) {
        List<Tournament> result = new ArrayList<>();
        for (Long tournamentId : allTournamentsOfPerson) {
            Tournament tournament = repository.findById(tournamentId)
                    .orElseThrow(EntityNotFoundException::new); // this should not happen
            result.add(tournament);
        }
        return result;
    }

    @Override
    protected boolean exists(Tournament tournament) {
        return repository.existsById(tournament.getTournamentId());
    }

    @Transactional
    public Tournament createTournament(NewTournament newTournament) {
        Tournament tournament = newTournament.getTournament();
        List<Long> competitors  = newTournament.getCompetitors();
        if (competitors.size() < 2)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "At least two competitors needed."
            );
        for (Long accountId : competitors) {
            if (!accountService.existsById(accountId))
                throw new EntityNotFoundException(
                        "Account with id " + accountId.toString() + "does not exist"
                );
            tournament.addAccount(accountService.findById(accountId));
        }
        if (!competitors.contains(tournament.getWinner()))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "the winner is not between competitors"
            );
        return repository.save(tournament);
    }

    public List<Account> findAllAccountsByTournamentId(Long id) {
        if (!existsById(id))
            throw new EntityNotFoundException();
        return accountService.findAllByTournamentId(id);
    }
}
