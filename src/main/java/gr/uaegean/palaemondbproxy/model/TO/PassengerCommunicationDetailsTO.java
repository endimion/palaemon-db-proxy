package gr.uaegean.palaemondbproxy.model.TO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerCommunicationDetailsTO {
    private String macAddress;
    private String[] preferredLanguage;
}
