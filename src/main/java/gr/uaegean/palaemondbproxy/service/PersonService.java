package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.DeviceInfo;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;

public interface PersonService {

    public void addDeviceToPerson(String personalIdentifier, DeviceInfo device, String clientId);
    public void addLocationToPerson(LocationTO location);
    public void deleteDeviceFromPerson(String personalIdentifier, DeviceInfo device);

    public void updateOxygenSaturation(String hashedMacAddress, String saturation);

}

