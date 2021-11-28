package cz.cvut.fit.tjv.bednaji2.tournamens.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.cvut.fit.tjv.bednaji2.tournamens.business.AccountService;
import cz.cvut.fit.tjv.bednaji2.tournamens.business.PersonService;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private PersonService personService;


    Person person1;
    Person personWithNoAccounts;
    Person nonExistingPerson;
    Account account1;
    Account account2;
    Account nonExistingAccount;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // Lets try some Strings.
    String invalidPerson = "{\n" +
            "        \"name\": \"test\",\n" +
            "        \"gender\": \"test\",\n" +
            "        \"dayOfBirth\": \"2000-07-28\",\n" +
            "        \"address\": {\n" +
            "            \"street\": \"3312 University Hill Road\",\n" +
            "            \"city\": \"Champaign\",\n" +
            "            \"postalCode\": \"61820\",\n" +
            "            \"phoneNumber\": \"217-58888889931-8556\"\n" + // <--- number is too long
            "        }}";
    Person invalidPersonObject;
    String validPerson = "{\"name\": \"validPerson\",\n" +
            "        \"gender\": \"test\",\n" +
            "        \"dayOfBirth\": \"2000-07-28\",\n" +
            "        \"address\": {\n" +
            "            \"street\": \"3312 University Hill Road\",\n" +
            "            \"city\": \"Champaign\",\n" +
            "            \"postalCode\": \"61820\",\n" +
            "            \"phoneNumber\": \"217-931-8556\"\n" +
            "        }}";
    Person validPersonObject;
    String validAccount = "{\n" +
            "    \"nick\": \"validAccount\",\n" +
            "    \"email\": \"test@gmail\",\n" +
            "    \"rank\": \"challenger\"\n" +
            "}";
    Account validAccountObject;
    String invalidAccount = "{\n" +
            "    \"nick\": \"invalidAccount\",\n" +
            "    \"email\": \"\",\n" +
            "    \"rank\": \"challenger\"\n" +
            "}";
    Account invalidAccountObject;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        person1 = Person.builder().personId(1L).build();
        personWithNoAccounts = Person.builder().personId(2L).build();
        nonExistingPerson = Person.builder().personId(3L).build();
        account1 = Account.builder().accountId(1L).build();
        account2 = Account.builder().accountId(2L).build();
        nonExistingAccount = Account.builder().accountId(3L).build();

        validPersonObject = objectMapper.readValue(validPerson, Person.class);
        invalidPersonObject = objectMapper.readValue(invalidPerson, Person.class);
        validAccountObject = objectMapper.readValue(validAccount, Account.class);
        invalidAccountObject = objectMapper.readValue(invalidAccount, Account.class);
    }

    @Test
    public void getAllPersonTest() throws Exception {
        List<Person> personList = List.of(person1, personWithNoAccounts);
        Mockito.when(personService.findAll()).thenReturn(personList);

        mockMvc.perform(get("/person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personId").value(person1.getPersonId()))
                .andExpect(jsonPath("$[1].personId").value(personWithNoAccounts.getPersonId()));
    }

    @Test
    public void getPersonTest() throws Exception {
        Mockito.when(personService.findById(person1.getPersonId())).thenReturn(person1);
        Mockito.when(personService.findById(nonExistingPerson.getPersonId())).thenThrow(
                new EntityNotFoundException());

        mockMvc.perform(get("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(person1.getPersonId()));

        mockMvc.perform(get("/person/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void createPersonTest() throws Exception {
        Mockito.when(personService.createPerson(validPersonObject)).thenReturn(validPersonObject);
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPerson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("validPerson"));

        mockMvc.perform(post("/person/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPerson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updatePersonTest() throws Exception {
        Mockito.doThrow(new EntityNotFoundException())
                .when(personService).updatePerson(nonExistingPerson.getPersonId(), validPersonObject);
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST))
                .when(personService).updatePerson(person1.getPersonId(), invalidPersonObject);
        Mockito.doNothing().when(personService).updatePerson(person1.getPersonId(), validPersonObject);

        mockMvc.perform(put("/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPerson))
                .andExpect(status().isAccepted());
        mockMvc.perform(put("/person/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPerson))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(put("/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPerson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deletePersonTest() throws Exception {
        Mockito.doThrow(new EntityNotFoundException())
                .when(personService).deletePersonById(nonExistingPerson.getPersonId());
        Mockito.doNothing().when(personService).deletePersonById(person1.getPersonId());
        mockMvc.perform(delete("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        mockMvc.perform(delete("/person/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createNewAccountTest() throws Exception {
        Mockito.when(personService.createNewAccount(nonExistingPerson.getPersonId(), validAccountObject))
                        .thenThrow(new EntityNotFoundException());
        Mockito.when(personService.createNewAccount(person1.getPersonId(), invalidAccountObject))
                        .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Mockito.when(personService.createNewAccount(person1.getPersonId(), validAccountObject))
                .thenReturn(validAccountObject);
        mockMvc.perform(post("/person/1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validAccount))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nick").value("validAccount"));
        mockMvc.perform(post("/person/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(post("/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAccount))
                .andExpect(status().is4xxClientError());
    }
}