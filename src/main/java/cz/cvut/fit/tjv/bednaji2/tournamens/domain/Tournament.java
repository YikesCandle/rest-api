package cz.cvut.fit.tjv.bednaji2.tournamens.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentId;

    @NotBlank(message = "invalid name format.")
    private String name;

    @NotNull
    private Long winner;

    @PastOrPresent(message = "Cannot register future tournament.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @JsonIgnore
    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "tournament_account_map",
            joinColumns = @JoinColumn(
                    name = "tournament_id",
                    referencedColumnName = "tournamentId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "account_id",
                    referencedColumnName = "accountId"
            )
    )
    private List<Account> accounts;

    public void addAccount(Account account) {
        if (accounts == null)
            accounts = new ArrayList<>();
        accounts.add(account);
    }
}
