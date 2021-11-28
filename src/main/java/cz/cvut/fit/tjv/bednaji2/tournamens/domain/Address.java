package cz.cvut.fit.tjv.bednaji2.tournamens.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverrides({
        @AttributeOverride(
                name = "street",
                column = @Column(name = "adress_street")
        ),
        @AttributeOverride(
                name = "city",
                column = @Column(name = "adress_city")
        ),
        @AttributeOverride(
                name = "postalCode",
                column = @Column(name = "adress_postalCode")
        ),
        @AttributeOverride(
                name = "phoneNumber",
                column = @Column(name = "adress_phoneNumber")
        )}
)
public class Address {
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotBlank
    private String postalCode;
    @NotBlank
    @Size(min = 6, max = 14, message = "invalid phone number format")
    private String phoneNumber;
}
