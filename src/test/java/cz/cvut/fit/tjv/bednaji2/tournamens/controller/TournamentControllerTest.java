package cz.cvut.fit.tjv.bednaji2.tournamens.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.cvut.fit.tjv.bednaji2.tournamens.business.TournamentService;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.NewTournament;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(TournamentController.class)
class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TournamentService tournamentService;


    Tournament validTournament;
    Tournament invalidTournament;
    NewTournament validNewTournament;
    NewTournament invalidNewTournament;
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<Long> validCompetitors = List.of(1L, 2L);
        validTournament = Tournament.builder().name("validTournament").winner(1L).build();
        invalidTournament = Tournament.builder().name("invalidTournament").build();
        validNewTournament = NewTournament.builder()
                .tournament(validTournament)
                .competitors(validCompetitors)
                .build();
        invalidNewTournament = NewTournament.builder()
                .tournament(invalidTournament)
                .competitors(validCompetitors)
                .build();
    }

    @Test
    public void findAllTournamentsTest() throws Exception {
        List<Tournament> tournamentList = List.of(validTournament);
        Mockito.when(tournamentService.findAll()).thenReturn(tournamentList);

        mockMvc.perform(get("/tournament")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(validTournament.getName()));
    }

    @Test
    public void createTournamentTest() throws Exception {
        Mockito.when(tournamentService.createTournament(invalidNewTournament))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Mockito.when(tournamentService.createTournament(validNewTournament))
                .thenReturn(validNewTournament.getTournament());

        mockMvc.perform(post("/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validNewTournament)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(validNewTournament.getTournament().getName()));
        mockMvc.perform(post("/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNewTournament)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void findTournamentTest() throws Exception {
        validTournament.setTournamentId(1L);
        invalidTournament.setTournamentId(2L);
        Mockito.when(tournamentService.findById(invalidTournament.getTournamentId()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Mockito.when(tournamentService.findById(validTournament.getTournamentId()))
                .thenReturn(validNewTournament.getTournament());
        mockMvc.perform(get("/tournament/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tournamentId").value(validTournament.getTournamentId()));
        mockMvc.perform(get("/tournament/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }


}