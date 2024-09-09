package dtos;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PersonDTO {
    private String firstName;
    private String lastName;
    private String birthDate;
    private AddressDTO address;
    private AccountDTO account;
}
