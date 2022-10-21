package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import gr.uaegean.palaemondbproxy.model.DutySchedule;
import gr.uaegean.palaemondbproxy.model.Personalinfo;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MinPersonTO {
    private String name;
    private String surname;
    private String gender;
    private String identifier;
    private String age;
    private String  embarkationPort;
    @JsonProperty("disembarkation_port")
    private  String disembarkationPort;
    private String ticketNumber;

    private String email;
    @JsonProperty("postal_address")
    private String postalAddress;
    @JsonProperty("emergency_contact_details")
    private String emergencyContact;
    @JsonProperty("country_of_residence")
    private String countryOfResidence;

    @JsonProperty("medical_condnitions")
    private String medicalCondition;
    @JsonProperty("mobility_issues")
    private String mobilityIssues;
    @JsonProperty("pregnency_data")
    private String prengencyData;

}
