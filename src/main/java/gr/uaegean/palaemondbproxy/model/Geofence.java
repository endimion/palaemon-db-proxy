package gr.uaegean.palaemondbproxy.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Document(indexName = "pameas-geofence-#{T(java.time.format.DateTimeFormatter).ofPattern(\"yyyy.MM.dd\").format(T(java.time.LocalDate).now())}" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Geofence implements Serializable {

    @Id
    private String id;

    @Field(type = Text)
    private String gfName;
    @Field(type = Text)
    private String deck;
    @Field(type=Text)
    private GeofenceStatusEnum status;

    @Field(type= FieldType.Boolean)
    private boolean isMusteringStation;



    public enum GeofenceStatusEnum {
        OPEN ("OPEN"),
        CONGESTED ("CONGESTED"),
        BLOCKED ("BLOCKED");

        private final String name;
        private GeofenceStatusEnum(String s) {
            name = s;
        }
        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }
        public String toString() {
            return this.name;
        }
    }

}
