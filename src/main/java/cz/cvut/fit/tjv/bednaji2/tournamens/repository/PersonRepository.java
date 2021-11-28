package cz.cvut.fit.tjv.bednaji2.tournamens.repository;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Person;
import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT t.tournament_id FROM tbl_tournament t " +
                    "JOIN tournament_account_map tam " +
                    "    ON t.tournament_id = tam.tournament_id " +
                    "JOIN tbl_account a " +
                    "    ON a.account_id = tam.account_id " +
                    "WHERE a.person_id= :personId")
    List<Long> findAllTournamentsOfPerson(Long personId); // tested in TournamentRepositoryTest
}
