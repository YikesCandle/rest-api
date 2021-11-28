package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.NewTournament;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import cz.cvut.fit.tjv.bednaji2.tournamens.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@SpringBootTest
class TournamentServiceTest {

    @Autowired
    private TournamentService tournamentService;

    @MockBean
    private TournamentRepository tournamentRepository;
    @MockBean
    private AccountService accountService;



    Tournament noCompetitorsTournament;
    Tournament accountDoesntExistTournament;
    Tournament winnerIsNotCompetitorTournament;
    Tournament allGoodTournament;

    Account existingAccount1;
    Account existingAccount2;
    Account nonExistingAccount;



    @BeforeEach
    public void setUp() {
        noCompetitorsTournament = Tournament.builder().tournamentId(1L).build();
        accountDoesntExistTournament = Tournament.builder().tournamentId(2L).build();
        winnerIsNotCompetitorTournament = Tournament.builder().tournamentId(3L).winner(3L).build();
        allGoodTournament = Tournament.builder().tournamentId(4L).winner(1L).build();

        existingAccount1 = Account.builder().accountId(1L).build();
        existingAccount2 = Account.builder().accountId(2L).build();
        nonExistingAccount = Account.builder().accountId(3L).build();

        Mockito.when(accountService.existsById(existingAccount1.getAccountId())).thenReturn(true);
        Mockito.when(accountService.existsById(existingAccount2.getAccountId())).thenReturn(true);
        Mockito.when(accountService.existsById(nonExistingAccount.getAccountId())).thenReturn(false);
        Mockito.when(accountService.findById(existingAccount1.getAccountId())).thenReturn(existingAccount1);
        Mockito.when(accountService.findById(existingAccount1.getAccountId())).thenReturn(existingAccount2);
        Mockito.when(tournamentRepository.save(allGoodTournament)).thenReturn(allGoodTournament);
    }

    @Test
    public void createTournamentTest() {

        Assertions.assertThrows(ResponseStatusException.class,
                () -> tournamentService.createTournament(
                        new NewTournament(noCompetitorsTournament, List.of())));
        Assertions.assertThrows(ResponseStatusException.class,
                () -> tournamentService.createTournament(
                        new NewTournament(winnerIsNotCompetitorTournament, List.of(1L, 2L))));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> tournamentService.createTournament(
                        new NewTournament(accountDoesntExistTournament, List.of(2L, 3L))));
        Assertions.assertEquals(allGoodTournament,
                tournamentService.createTournament(
                        new NewTournament(allGoodTournament, List.of(1L, 2L))));
    }
}