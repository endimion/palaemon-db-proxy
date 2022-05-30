package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EvacuatorRequest {

    private String originator;
    private String current;
    private String previous;
    private String timestamp;
}
