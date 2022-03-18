package gr.uaegean.palaemondbproxy.model.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserLocation { //denotes the history of specific client

    private String macAddress; // link to a client Id in practice.
    private String hashedMacAddress;
    private List<UserLocationUnit> locationHistory;


}