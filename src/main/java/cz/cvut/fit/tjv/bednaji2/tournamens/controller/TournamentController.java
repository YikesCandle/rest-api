package cz.cvut.fit.tjv.bednaji2.tournamens.controller;

import cz.cvut.fit.tjv.bednaji2.tournamens.business.TournamentService;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.NewTournament;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "tournament")
public class TournamentController {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public List<Tournament> findAllTournaments() {
        return tournamentService.findAll();
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Tournament createTournament(@Valid @RequestBody NewTournament newTournament) {
        return tournamentService.createTournament(newTournament);
    }

    @GetMapping("{tournamentId}")
    public Tournament findTournament(@PathVariable("tournamentId") Long id) {
        return tournamentService.findById(id);
    }

    @GetMapping("{tournamentId}/account")
    public List<Account> findAllAccountsOfTournament(@PathVariable("tournamentId") Long id) {
        return tournamentService.findAllAccountsByTournamentId(id);
    }
}
