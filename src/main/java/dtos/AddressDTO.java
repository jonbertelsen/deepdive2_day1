package dtos;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddressDTO {
    private String street;
    private String city;
    private int zipCode;
}

