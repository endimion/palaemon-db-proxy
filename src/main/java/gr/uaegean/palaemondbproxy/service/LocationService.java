package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.DeviceInfo;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;

public interface LocationService {

    public void addLocationAndGeofenceToPerson(String macAddress, LocationTO location);


}

