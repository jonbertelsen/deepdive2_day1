package dtos;

import lombok.Data;

@Data
public class CurrentDataDTO {
    private double temperature;
    private String skyText;
    private String humidity;
    private String windText;
}
