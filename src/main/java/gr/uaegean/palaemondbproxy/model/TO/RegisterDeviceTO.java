package gr.uaegean.palaemondbproxy.model.TO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterDeviceTO {

    /*
     "macAddress": "retrieved and hashed macAddress with sha256",
   "imsi": "retrieved imsi",
   "msisdn": "retrieved msisdn",
   "imei": "retrieved imei",
    "mumbleName": "user selected name"

     */

    private String macAddress;
    private String hashedMacAddress;
    private String imsi;
    private String msisdn;
    private String imei;
    private String mumbleName;
}
