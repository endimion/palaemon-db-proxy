package gr.uaegean.palaemondbproxy.model.TO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MinLocationTO implements Serializable {

/*
{
"hashedMacAddress":"12313122321",
"geofence":{
"gfId":"121312",
"gfName":"testf",
"timestamp":"1232132121321211111"
},
"location":{
"xLocation":"12321",
"yLocation":"12312312",
"timestamp":"1232132121321211111"
}
}
 */
    private String hashedMacAddress;
    private MinLocationUnitTO location;
    private MinGeofenceUnitTO geofence;
    private String gfName;
}
