package cz.cvut.fit.tjv.bednaji2.tournamens.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.bednaji2.tournamens.controller.Views;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_person")
public class Person {
    @JsonView(Views.Summary.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @JsonView(Views.Summary.class)
    @NotNull(message = "Person name cannot be null.")
    @NotBlank(message = "Person name cannot be empty.")
    @Size(max = 32, min = 3, message = "Invalid name size.")
    private String name;

    private String gender;

    @Past(message = "Day of birth cannot be future or present.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dayOfBirth;

    @Embedded
    @Valid
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    List<Account> accounts;

    @Transient
    private Integer age;

    @JsonView(Views.Summary.class)
    @Transient
    private Integer numberOfAccounts;

    public Integer getAge() {
        if (this.dayOfBirth == null)
            return null;
        return Period.between(dayOfBirth, LocalDate.now()).getYears();
    }

    public Integer getNumberOfAccounts() {
        if (accounts == null)
            return 0;
        return accounts.size();
    }
}
