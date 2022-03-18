package gr.uaegean.palaemondbproxy.model.TO;

import gr.uaegean.palaemondbproxy.model.Geofence;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LatestLocationTO {

    UserLocationUnit location;
    Geofence geofence ;
}
