package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MinGeofenceUnitTO {
    private String gfId;
    private String gfName;
    private String timestamp;
    private String deck;
}
