package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PassengerPIMMTO {
    @JsonProperty("first_name")
    private String fistName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("messaging_id")
    private String messagingId;


}
