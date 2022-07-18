package gr.uaegean.palaemondbproxy.model.TO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PassengerMusteringStatusTO {

    //how many passengers are assigned to this geofence
    private int passengersAssigned;

    // how many passengers are at the geofence in the given moment
    private int passengersMustered;

    //how many passengers are assigned to this MS but are not there
    private int passengersMissing;
}
