TJV Semestrální Práce -- 2. kontrolní bod

Entity: Person
        Account
        Tournament
        
        *Address

Vztahy:  Person -> 1:N -> Account
        Account -> N:N -> Tournament
        
        *Person -> 1:1 -> Address

Funkce: Person:     
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
            