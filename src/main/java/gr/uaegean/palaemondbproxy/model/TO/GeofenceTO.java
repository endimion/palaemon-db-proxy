package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import gr.uaegean.palaemondbproxy.model.Geofence;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeofenceTO implements Serializable {
    private String id;
    private String gfName;
    private String deck;
    private Geofence.GeofenceStatusEnum status;
    @JsonProperty("mustering")
    private boolean isMusteringStation;
    private String srapZone;
}
