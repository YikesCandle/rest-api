**TJV Semestrální Práce -- 2. kontrolní bod**

Entity: 

        Person
        Account
        Tournament
        
        *Address

Vztahy:

        Person  -> 1:N -> Account       // Jedna osoba může mít několik účtů
        Account -> N:N -> Tournament    // Jeden účet se může zúčastnot několika turnajů
                                        // Jeden turnaj má několik zúčastněných účtů
        
        *Person -> 1:1 -> Address       // Jedna adresa na jednu osobu

Funkce:

        Person:     
            create  :   createPerson        /person
            read    :   findAllPersonons    /person
                        findPerson          /person/{personID}
            update  :   updatePerson        /person/{personID}
            delete  :   deletePerson        /person/{personID}
            
        Account:
            create  :   createNewAccount    /person/{personID}/account
            read    :   findAllAccounts     /account
                        findAccount         /account/{accountID}
                        allPersonAccounts   /person/{personID}/account
                        personAccount       /person/{personID}/account/{accountID}
            update  :   updateAccount       /person/{personID}/account/{accountID}
            delete  :   UNSUPORTED
            
        Tournament:
            create  :   createNewTournament /tournament
            read    :   findAllTournaments  /tournament
                        findTournament      /tournament/{tournamentID}
                        allPersonTrour..    /person/{personid}/tournament   // Native querry
            update  :   UNSUPORTED
            delete  :   UNSUPORTED
            
Databáze: Local Postgres

Poznámky: errory nemám moc vychytané, testy +- něco dělají
            