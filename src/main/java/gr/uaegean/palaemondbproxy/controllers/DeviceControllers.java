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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DeviceControllers {

    @Autowired
    PersonService personService;


    @Autowired
    ElasticService elasticService;


    @PostMapping("/addDevice")
    public @ResponseBody String addDeviceInfoToPerson(@RequestBody AddDevicePersonTO addDevicePersonTO) {
        log.info("adding device based on request {}", addDevicePersonTO);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setMsisdn(addDevicePersonTO.getMsisdn());
        deviceInfo.setImsi(addDevicePersonTO.getImsi());
        deviceInfo.setImei(addDevicePersonTO.getImei());
        deviceInfo.setMacAddress(addDevicePersonTO.getMacAddress());
//        deviceInfo.setBraceletId(addDevicePersonTO.getBraceletId());
        final MessageDigest digest;
        String sha256hex = DigestUtils.sha256Hex(addDevicePersonTO.getMacAddress());
        deviceInfo.setHashedMacAddress(sha256hex);

        if (StringUtils.isEmpty(addDevicePersonTO.getTicketNumber())) {
            this.personService.addDeviceToPerson(addDevicePersonTO.getIdentifier(), deviceInfo,
                    addDevicePersonTO.getMessagingAppClientId(), addDevicePersonTO.getBraceletId());
            return "OK";
        }

        this.personService.addDeviceToPersonByTicketNumber(addDevicePersonTO.getTicketNumber(), deviceInfo,
                addDevicePersonTO.getMessagingAppClientId(), addDevicePersonTO.getBraceletId());
        return "OK";

    }


    @PostMapping("/registerDevice")
    public @ResponseBody String registerDevice(@RequestBody RegisterDeviceTO registerDeviceTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        log.info("adding device based on request {}", registerDeviceTO);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setMsisdn(registerDeviceTO.getMsisdn());
        deviceInfo.setImsi(registerDeviceTO.getImsi());
        deviceInfo.setImei(registerDeviceTO.getImei());
        deviceInfo.setMacAddress(registerDeviceTO.getMacAddress());
//        deviceInfo.setBraceletId(addDevicePersonTO.getBraceletId());
        final MessageDigest digest;
        String sha256hex = DigestUtils.sha256Hex(registerDeviceTO.getMacAddress());
        deviceInfo.setHashedMacAddress(sha256hex);

        if (StringUtils.isEmpty(registerDeviceTO.getTicketNumber())) {
            this.personService.addDeviceUsingMumbleName(registerDeviceTO.getMumbleName(), deviceInfo);
            return "OK";
        }

        this.personService.addDeviceToPersonByTicketNumber(registerDeviceTO.getTicketNumber(), deviceInfo,
                registerDeviceTO.getTicketNumber(), "");
        return "OK";
    }


    public List<DeviceInfo> getDevicesofPerson() {
        return null;
    }


    @GetMapping("/getAllDeviceIdRole")
    public @ResponseBody
    Map<String, String> getAllDeviceIdRole() {
        Map<String, String> result = new HashMap<>();

        elasticService.getAllPersonsDecrypted().stream().forEach(person -> {
            if(person.getNetworkInfo()!=null && person.getNetworkInfo().getDeviceInfoList() != null &&
                    person.getNetworkInfo().getDeviceInfoList().size() > 0
            ){
                int size = person.getNetworkInfo().getDeviceInfoList().size();
                String deviceId = person.getNetworkInfo().getDeviceInfoList().get(size-1).getMacAddress();
                String role = person.getPersonalInfo().isCrew()?"crew":"passenger";
                result.put(deviceId,role);
            }
        });
        return result;
    }

}
