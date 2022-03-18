package gr.uaegean.palaemondbproxy.model;

import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationInfo  implements Serializable {
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<UserGeofenceUnit> geofenceHistory;
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<UserLocationUnit> locationHistory;
}
