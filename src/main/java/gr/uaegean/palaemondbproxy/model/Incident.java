package gr.uaegean.palaemondbproxy.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Document(indexName = "pameas-incident-#{T(java.time.format.DateTimeFormatter).ofPattern(\"yyyy.MM.dd\").format(T(java.time.LocalDate).now())}")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Incident implements Serializable {

    @Id
    private String id;
    @Field(type = Text)
    private String passengerName;
    @Field(type = Text)
    private String passengerSurname;
    @Field(type = Text)
    private String healthIssues;
    @Field(type = Text)
    private String mobilityIssues;
    @Field(type = Text)
    private String pregnancyStatus;
    @Field(type = Text)
    private String assignedCrewMemberIdDecrypted;
    @Field(type = Text)
    private String xLoc;
    @Field(type = Text)
    private String yLoc;
    @Field(type = Text)
    private String[] preferredLanguage;
    @Field(type = Text)
    private String geofenceId;
    @Field(type = Text)
    private String deck;
    @Field(type = Text)
    private String timestamp;
    @Field(type = Text)
    private IncidentStatus status;
    @Field(type = Text)
    private String incidentId;


    public enum IncidentStatus {
        OPEN("OPEN"),
        CLOSED("CLOSED"),
        ASSIGNED("ASSIGNED");
        private final String name;
        private IncidentStatus(String s) {
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
