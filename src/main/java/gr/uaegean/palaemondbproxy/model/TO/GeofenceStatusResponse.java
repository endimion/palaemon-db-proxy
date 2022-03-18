package gr.uaegean.palaemondbproxy.model.TO;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GeofenceStatusResponse {
    private ArrayList<GeofenceTO> mustering;
    private ArrayList<GeofenceTO> simple;
}
