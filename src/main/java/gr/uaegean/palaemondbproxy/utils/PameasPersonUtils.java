package gr.uaegean.palaemondbproxy.utils;

import gr.uaegean.palaemondbproxy.model.PameasPerson;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class PameasPersonUtils {


    // keeps the database (ES) id of the object and pushes all the rest
    public static PameasPerson updatePerson(PameasPerson originalPerson, PameasPerson nPerson, CryptoUtils cryptoUtils) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //update personal info
//        originalPerson.getPersonalInfo().setName(cryptoUtils.encryptBase64(nPerson.getPersonalInfo().getName()));
//        originalPerson.getPersonalInfo().setSurname(cryptoUtils.encryptBase64(nPerson.getPersonalInfo().getSurname()));
//        originalPerson.getPersonalInfo().setPersonalId(cryptoUtils.encryptBase64(nPerson.getPersonalInfo().getPersonalId()));
        originalPerson.getPersonalInfo().setGender(nPerson.getPersonalInfo().getGender());
        originalPerson.getPersonalInfo().setDateOfBirth(nPerson.getPersonalInfo().getDateOfBirth());
        originalPerson.getPersonalInfo().setTicketNumber(nPerson.getPersonalInfo().getTicketNumber());

        originalPerson.getPersonalInfo().setTicketInfo(nPerson.getPersonalInfo().getTicketInfo());
        originalPerson.getPersonalInfo().setDateOfBirth(nPerson.getPersonalInfo().getDateOfBirth());
        originalPerson.getPersonalInfo().setEmbarkationPort(nPerson.getPersonalInfo().getEmbarkationPort());
        originalPerson.getPersonalInfo().setDisembarkationPort(nPerson.getPersonalInfo().getDisembarkationPort());
        originalPerson.getPersonalInfo().setEmail(nPerson.getPersonalInfo().getEmail());
        originalPerson.getPersonalInfo().setCountryOfResidence(nPerson.getPersonalInfo().getCountryOfResidence());
        originalPerson.getPersonalInfo().setPreferredLanguage(nPerson.getPersonalInfo().getPreferredLanguage());
        originalPerson.getPersonalInfo().setMedicalCondition(nPerson.getPersonalInfo().getMedicalCondition());
        originalPerson.getPersonalInfo().setMobilityIssues(nPerson.getPersonalInfo().getMobilityIssues());
        originalPerson.getPersonalInfo().setPrengencyData(nPerson.getPersonalInfo().getPrengencyData());
        originalPerson.getPersonalInfo().setEmergencyContact(nPerson.getPersonalInfo().getEmergencyContact());
        originalPerson.getPersonalInfo().setCrew(nPerson.getPersonalInfo().isCrew());
        originalPerson.getPersonalInfo().setRole(nPerson.getPersonalInfo().getRole());
        originalPerson.getPersonalInfo().setEmergencyDuty(nPerson.getPersonalInfo().getEmergencyDuty());
        originalPerson.getPersonalInfo().setHeartBeat(nPerson.getPersonalInfo().getHeartBeat());
        originalPerson.getPersonalInfo().setOxygenSaturation(nPerson.getPersonalInfo().getOxygenSaturation());


        //update device info
        if (originalPerson.getNetworkInfo() == null) {
            originalPerson.setNetworkInfo(nPerson.getNetworkInfo());
        } else {
            originalPerson.getNetworkInfo().setDeviceInfoList(nPerson.getNetworkInfo().getDeviceInfoList());
            if (originalPerson.getNetworkInfo().getMessagingAppClientId() != null &&
                    !originalPerson.getNetworkInfo().getMessagingAppClientId().equals(nPerson.getNetworkInfo().getMessagingAppClientId()))
                log.info("updating {} with {}", originalPerson.getNetworkInfo().getMessagingAppClientId(),
                        nPerson.getNetworkInfo().getMessagingAppClientId());
            originalPerson.getNetworkInfo().setMessagingAppClientId(nPerson.getNetworkInfo().getMessagingAppClientId());
            originalPerson.getNetworkInfo().setBraceletId(nPerson.getNetworkInfo().getBraceletId());
        }
        if (originalPerson.getLocationInfo() == null) {
            originalPerson.setLocationInfo(nPerson.getLocationInfo());
        } else {
            originalPerson.getLocationInfo().setLocationHistory(nPerson.getLocationInfo().getLocationHistory());
            originalPerson.getLocationInfo().setGeofenceHistory(nPerson.getLocationInfo().getGeofenceHistory());
            originalPerson.getLocationInfo().setSpeed(nPerson.getLocationInfo().getSpeed());
        }
        return originalPerson;

    }
}
