package dtos;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountDTO {
    private String id;
    private String balance;
    @JsonSetter("isActive")
    private boolean isActive;
}

