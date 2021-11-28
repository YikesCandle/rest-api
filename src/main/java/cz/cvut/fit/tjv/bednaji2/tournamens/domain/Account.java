package cz.cvut.fit.tjv.bednaji2.tournamens.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.bednaji2.tournamens.controller.Views;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_account")
public class Account {
    @JsonView(Views.Summary.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @JsonView(Views.Summary.class)
    @NotBlank(message = "Nick of account is cannot be blank.")
    @NotNull(message = "Nick of account is cannot be null.")
    @Size(max = 16, min = 4, message = "Invalid nick size.")
    private String nick;

    @Column(unique = true, nullable = false)
    String email;
    @JsonView(Views.Summary.class)
    private String rank;

    @JsonIgnore
    @JsonView(Views.SummaryNoPerson.class)
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "person_id",
            referencedColumnName = "personId"
    )
    private Person person;

    @JsonIgnore
    @ManyToMany(mappedBy="accounts", cascade = CascadeType.ALL)
    private List<Tournament> tournaments;
}
