package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.DeviceInfo;
import gr.uaegean.palaemondbproxy.model.LocationInfo;
import gr.uaegean.palaemondbproxy.model.NetworkInfo;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.AddDevicePersonTO;
import gr.uaegean.palaemondbproxy.model.TO.RegisterDeviceTO;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@Slf4j
public class DeviceControllers {

    @Autowired
    PersonService personService;


    @PostMapping("/addDevice")
    public void addDeviceInfoToPerson(@RequestBody AddDevicePersonTO addDevicePersonTO) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setMsisdn(addDevicePersonTO.getMsisdn());
        deviceInfo.setImsi(addDevicePersonTO.getImsi());
        deviceInfo.setImei(addDevicePersonTO.getImei());
        deviceInfo.setMacAddress(addDevicePersonTO.getMacAddress());
//        deviceInfo.setBraceletId(addDevicePersonTO.getBraceletId());
        final MessageDigest digest;
        String sha256hex = DigestUtils.sha256Hex(addDevicePersonTO.getMacAddress());
        deviceInfo.setHashedMacAddress(sha256hex);
        this.personService.addDeviceToPerson(addDevicePersonTO.getIdentifier(), deviceInfo,
                addDevicePersonTO.getMessagingAppClientId(), addDevicePersonTO.getBraceletId());
    }


    @PostMapping("/registerDevice")
    public void registerDevice(@RequestBody RegisterDeviceTO registerDeviceTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setMsisdn(registerDeviceTO.getMsisdn());
        deviceInfo.setImsi(registerDeviceTO.getImsi());
        deviceInfo.setImei(registerDeviceTO.getImei());
        deviceInfo.setMacAddress(registerDeviceTO.getMacAddress());
//        deviceInfo.setBraceletId(addDevicePersonTO.getBraceletId());
        final MessageDigest digest;
        String sha256hex = DigestUtils.sha256Hex(registerDeviceTO.getMacAddress());
        deviceInfo.setHashedMacAddress(sha256hex);
        this.personService.addDeviceUsingMumbleName(registerDeviceTO.getMumbleName(), deviceInfo);
    }


    public List<DeviceInfo> getDevicesofPerson() {
        return null;
    }
}
