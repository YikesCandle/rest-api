package cz.cvut.fit.tjv.bednaji2.tournamens.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTournament {
    @Valid
    public Tournament tournament;
    public List<Long> competitors;
}
