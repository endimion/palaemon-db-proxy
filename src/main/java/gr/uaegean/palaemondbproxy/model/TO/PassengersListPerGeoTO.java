package gr.uaegean.palaemondbproxy.model.TO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PassengersListPerGeoTO {
    private String geofence;
    @JsonProperty("geofence_label")
    private String label;
    private List<PassengerPIMMTO> passengers;
}
