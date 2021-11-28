package cz.cvut.fit.tjv.bednaji2.tournamens.repository;

import cz.cvut.fit.tjv.bednaji2.tournamens.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
