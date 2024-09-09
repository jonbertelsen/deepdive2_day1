package dtos;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Setter;

@Data
public class LocationDTO {
    @JsonSetter("LocationName")
    private String locationName;
    @JsonSetter("CurrentData")
    private CurrentDataDTO currentData;
}
