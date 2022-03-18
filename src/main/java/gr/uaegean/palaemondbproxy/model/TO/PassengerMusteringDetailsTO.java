package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerMusteringDetailsTO
{
    @JsonProperty("general_info")
    private PersonTO generalInfo ;

    @JsonProperty("last_known_location")
    private LatestLocationTO lastKnowLocation;

    @JsonProperty("communication_details")
    private PassengerCommunicationDetailsTO communicationDetails;

}
