package gr.uaegean.palaemondbproxy.service.impl;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import gr.uaegean.palaemondbproxy.model.DeviceInfo;
import gr.uaegean.palaemondbproxy.model.LocationInfo;
import gr.uaegean.palaemondbproxy.model.NetworkInfo;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationHealthTO;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.service.PersonService;
import gr.uaegean.palaemondbproxy.service.SpeedService;
import gr.uaegean.palaemondbproxy.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    @Autowired
    private ElasticService elasticService;

    @Autowired
    private CryptoUtils cryptoUtils;

    @Autowired
    SpeedService speedService;


    @Override
    public void addDeviceToPerson(String personalIdentifier, DeviceInfo device, String clientId, String braceletId) {
        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByPersonalIdentifierDecrypted(personalIdentifier);
        if (existingPerson.isPresent()) {
            if (existingPerson.get().getNetworkInfo() == null) {
                NetworkInfo networkInfo = new NetworkInfo();
                networkInfo.setDeviceInfoList(new ArrayList<>());
                existingPerson.get().setNetworkInfo(networkInfo);
            }
            if (existingPerson.get().getLocationInfo() == null) {
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setLocationHistory(new ArrayList<>());
                locationInfo.setGeofenceHistory(new ArrayList<>());
                existingPerson.get().setLocationInfo(locationInfo);
            }
            if (existingPerson.get().getNetworkInfo().getDeviceInfoList().stream()
                    .filter(d -> d.getMacAddress().equals(device.getMacAddress())).findFirst().isEmpty()) {
                existingPerson.get().getNetworkInfo().getDeviceInfoList().add(device);
            }
            if (braceletId != null) {
                existingPerson.get().getNetworkInfo().setBraceletId(braceletId);
            }

            if (StringUtils.isNotBlank(clientId)) {
                existingPerson.get().getNetworkInfo().setMessagingAppClientId(clientId);
            }
            elasticService.updatePerson(personalIdentifier, existingPerson.get());
        } else {
            log.info("could not find user with PersonalIdentifier  {}", personalIdentifier);
        }
    }

    @Override
    public void addDeviceToPersonByTicketNumber(String ticketNumber, DeviceInfo device, String clientId, String braceletId) {
        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByTicketNumber(ticketNumber);
        if (existingPerson.isPresent()) {
            if (existingPerson.get().getNetworkInfo() == null) {
                NetworkInfo networkInfo = new NetworkInfo();
                networkInfo.setDeviceInfoList(new ArrayList<>());
                existingPerson.get().setNetworkInfo(networkInfo);
            }
            if (existingPerson.get().getLocationInfo() == null) {
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setLocationHistory(new ArrayList<>());
                locationInfo.setGeofenceHistory(new ArrayList<>());
                existingPerson.get().setLocationInfo(locationInfo);
            }
            if (existingPerson.get().getNetworkInfo().getDeviceInfoList().stream()
                    .filter(d -> d.getMacAddress().equals(device.getMacAddress())).findFirst().isEmpty()) {
                existingPerson.get().getNetworkInfo().getDeviceInfoList().add(device);
            }
            if (braceletId != null) {
                existingPerson.get().getNetworkInfo().setBraceletId(braceletId);
            }

            if (StringUtils.isNotBlank(clientId)) {
                existingPerson.get().getNetworkInfo().setMessagingAppClientId(clientId);
            }
            String decryptedPersonalIdentifier =
                    null;
            try {
                decryptedPersonalIdentifier = this.cryptoUtils.decryptBase64Message(existingPerson.get().getPersonalInfo().getPersonalId());
                elasticService.updatePerson(decryptedPersonalIdentifier, existingPerson.get());
            } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                     NoSuchAlgorithmException e) {
                log.error(e.getMessage());
            }
        } else {
            log.info("could not find user with TicketNumber  {}",ticketNumber);
        }
    }


    @Override
    public void addDeviceUsingMumbleName(String mumbleName, DeviceInfo device) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByMacAddress(device.getMacAddress());
        if (existingPerson.isPresent()) {
            if (existingPerson.get().getNetworkInfo() == null) {
                NetworkInfo networkInfo = new NetworkInfo();
                networkInfo.setDeviceInfoList(new ArrayList<>());
                existingPerson.get().setNetworkInfo(networkInfo);
            }
            if (existingPerson.get().getLocationInfo() == null) {
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setLocationHistory(new ArrayList<>());
                locationInfo.setGeofenceHistory(new ArrayList<>());
                existingPerson.get().setLocationInfo(locationInfo);
            }
            if (existingPerson.get().getNetworkInfo().getDeviceInfoList().stream()
                    .filter(d -> d.getMacAddress().equals(device.getMacAddress())).findFirst().isEmpty()) {
                existingPerson.get().getNetworkInfo().getDeviceInfoList().add(device);
            }

            existingPerson.get().getNetworkInfo().setMessagingAppClientId(mumbleName);
            String personalIDDecrypted = this.cryptoUtils.decryptBase64Message(existingPerson.get().getPersonalInfo().getPersonalId());

            elasticService.updatePerson(personalIDDecrypted, existingPerson.get());
        } else {
            log.info("could not find user with hashedMacAddress {}", device.getMacAddress());
        }

    }


    @Override
    public void addLocationToPerson(LocationTO location) {
        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByHashedMacAddress(location.getHashedMacAddress());
        if (existingPerson.isPresent()) {
            PameasPerson person = existingPerson.get();
            if (person.getLocationInfo().getLocationHistory() == null) {
                person.getLocationInfo().setLocationHistory(new ArrayList<>());
            }
            if (person.getLocationInfo().getGeofenceHistory() == null) {
                person.getLocationInfo().setGeofenceHistory(new ArrayList<>());
            }
            person.getLocationInfo().getLocationHistory().add(location.getLocation());
            person.getLocationInfo().getGeofenceHistory().add(location.getGeofence());
            try {
                String decryptedPersonaId = cryptoUtils.decryptBase64Message(person.getPersonalInfo().getPersonalId());

                double speed = speedService.updatePersonSpeed(location);
                if (speed > 0 && speed <= 90) {
                    person.getLocationInfo().setSpeed(String.valueOf(speed));
                    log.info("SPEED successfully set to {}", person.getLocationInfo().getSpeed());
                } else {
                    log.error("error calculating speed {}", speed);
                }

                this.elasticService.updatePerson(decryptedPersonaId, person);

            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                log.error(e.getMessage());
            }
        }
    }


    @Override
    public void addLocationHealthToPerson(LocationHealthTO location) {
        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByHashedMacAddress(location.getHashedMacAddress());
        if (existingPerson.isPresent()) {
            PameasPerson person = existingPerson.get();
            if (person.getLocationInfo().getLocationHistory() == null) {
                person.getLocationInfo().setLocationHistory(new ArrayList<>());
            }
            if (person.getLocationInfo().getGeofenceHistory() == null) {
                person.getLocationInfo().setGeofenceHistory(new ArrayList<>());
            }
            person.getLocationInfo().getLocationHistory().add(location.getLocation());
            person.getLocationInfo().getGeofenceHistory().add(location.getGeofence());
            person.getPersonalInfo().setOxygenSaturation(location.getSaturation());
            person.getPersonalInfo().setHeartBeat(location.getHeartBeat());

            try {
                String decryptedPersonaId = cryptoUtils.decryptBase64Message(person.getPersonalInfo().getPersonalId());
                LocationTO loc = new LocationTO();
                loc.setLocation(location.getLocation());
                loc.setGeofence(location.getGeofence());
                loc.setHashedMacAddress(location.getHashedMacAddress());
                loc.setMacAddress(location.getMacAddress());
                double speed = speedService.updatePersonSpeed(loc);
                if (speed > 0 && speed <= 90) {
                    person.getLocationInfo().setSpeed(String.valueOf(speed));
                    log.info("SPEED successfully set to {}", person.getLocationInfo().getSpeed());
                } else {
                    log.error("error calculating speed {}", speed);
                }

                this.elasticService.updatePerson(decryptedPersonaId, person);

            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                log.error(e.getMessage());
            }
        }
    }


    @Override
    public void deleteDeviceFromPerson(String personalIdentifier, DeviceInfo device) {
        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByPersonalIdentifierDecrypted(personalIdentifier);
        if (existingPerson.isPresent()) {
            List<DeviceInfo> oldDevices = existingPerson.get().getNetworkInfo().getDeviceInfoList();
            List<DeviceInfo> newDevices = new ArrayList<>();
            oldDevices.forEach(d -> {
                if (!d.getMacAddress().equals(device.getMacAddress())) {
                    newDevices.add(device);
                }
            });
            existingPerson.get().getNetworkInfo().setDeviceInfoList(newDevices);
            elasticService.updatePerson(personalIdentifier, existingPerson.get());
        } else {
            log.info("could not find user with PersonalIdentifier  {}", personalIdentifier);
        }
    }

    @Override
    public void updateOxygenSaturation(String braceletId, String saturation) {
        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByBraceletId(braceletId);
        if (existingPerson.isPresent()) {
            existingPerson.get().getPersonalInfo().setOxygenSaturation(saturation);
            try {
                String decryptedPersonaId = cryptoUtils.decryptBase64Message(existingPerson.get().getPersonalInfo().getPersonalId());
                elasticService.updatePerson(decryptedPersonaId, existingPerson.get());
            } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                     NoSuchAlgorithmException e) {
                log.error(e.getMessage());
            }


        }
    }
}
