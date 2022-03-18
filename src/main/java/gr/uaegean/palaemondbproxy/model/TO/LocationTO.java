package gr.uaegean.palaemondbproxy.model.TO;

import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationTO  implements Serializable {

    private String macAddress;
    private String hashedMacAddress;
    private UserGeofenceUnit geofence;
    private UserLocationUnit location;
}
