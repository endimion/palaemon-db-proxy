package gr.uaegean.palaemondbproxy.model.TO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddDevicePersonTO {

    private String identifier;
    private String macAddress;
    private String hashedMacAddress;
    private String imsi;
    private String msisdn;
    private String imei;
    private String messagingAppClientId;
}
