package gr.uaegean.palaemondbproxy.model.TO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PassengersPerGeofenceTO {

    private String geofence;
    private List<MinPersonTO> persons;
}
