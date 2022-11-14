package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.DeviceInfo;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationHealthTO;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface PersonService {

    public void addDeviceToPerson(String personalIdentifier, DeviceInfo device, String clientId, String braceletId);
    public void addLocationToPerson(LocationTO location);
    public void addLocationHealthToPerson(LocationHealthTO location);

    public void deleteDeviceFromPerson(String personalIdentifier, DeviceInfo device);

    public void updateOxygenSaturation(String hashedMacAddress, String saturation);
    public void addDeviceUsingMumbleName(String mumbleName, DeviceInfo device) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

}

