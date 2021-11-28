package cz.cvut.fit.tjv.bednaji2.tournamens.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.bednaji2.tournamens.business.AccountService;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "account")
public class AccountController {
    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @JsonView(Views.Summary.class)
    @GetMapping
    public List<Account> findAllAccounts() {
        return accountService.findAll();
    }

    @GetMapping("{accountId}")
    public Account findAccount(@PathVariable("accountId") Long id) {
        return accountService.findById(id);
    }
}
