package gr.uaegean.palaemondbproxy.service.impl;

import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.service.SpeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class SpeedServiceImpl implements SpeedService {

    @Autowired
    ElasticService elasticService;

    @Override
    public double calculateDistance(double x1, double x2, double y1, double y2) {
        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);

        return Math.hypot(ac, cb);
    }


    @Override
    public double calculateSpeed(double distance, long t1, long t2) {
        double timeElapsed = Math.abs(t2 - t1);
        return (distance / timeElapsed) * 60;
    }

    @Override
    public double updatePersonSpeed(LocationTO location) {
        Optional<PameasPerson> existingPerson = this.elasticService.getPersonByHashedMacAddress(location.getHashedMacAddress());
        if (existingPerson.isPresent()) {
            PameasPerson person = existingPerson.get();
            // if locations already exist
            if (person.getLocationInfo().getLocationHistory() != null && person.getLocationInfo().getLocationHistory().size() >= 1
                    && person.getLocationInfo().getGeofenceHistory() != null && person.getLocationInfo().getGeofenceHistory().size() >= 1) {
                int lastLocationIndex = person.getLocationInfo().getLocationHistory().size() - 1;
                String oldXString = person.getLocationInfo().getLocationHistory().get(lastLocationIndex).getXLocation();
                String oldYString = person.getLocationInfo().getLocationHistory().get(lastLocationIndex).getYLocation();
                //2022-07-04 11:47:03
                String oldTimestamp = person.getLocationInfo().getLocationHistory().get(lastLocationIndex).getTimestamp();
                SimpleDateFormat sdfu = null;
                if (oldTimestamp.toLowerCase().indexOf("z") > 0) {
                    sdfu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                } else {
                    sdfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                Date udate = null;
                try {
                    udate = sdfu.parse(oldTimestamp);
                    long timeInMillisSinceEpoch123 = udate.getTime();
                    long durationinSeconds2 = timeInMillisSinceEpoch123 / 1000;

                    udate = sdfu.parse(location.getLocation().getTimestamp());
                    long timeInMillisSinceEpochCurrent = udate.getTime();
                    long durationinSecondsCurrent = timeInMillisSinceEpochCurrent / 1000;

                    String oldDeck = person.getLocationInfo().getGeofenceHistory().get(person.getLocationInfo().getGeofenceHistory().size() - 1).getDeck();
                    //if we didnt change decks update the speed
                    if (oldDeck != null && oldDeck.equals(location.getGeofence().getDeck())) {
                        double oldXDouble = Double.parseDouble(oldXString);
                        double oldYDouble = Double.parseDouble(oldYString);
                        double currentXDouble = Double.parseDouble(location.getLocation().getXLocation());
                        double currentYDouble = Double.parseDouble(location.getLocation().getYLocation());

                        double speed = this.calculateSpeed(this.calculateDistance(oldXDouble, currentXDouble, oldYDouble, currentYDouble),
                                durationinSeconds2, durationinSecondsCurrent);
//                        log.info("Calculated the speed {}", speed);
                        return speed;
                    }

                } catch (ParseException e) {
                    log.error("couldn't parse date {}", e.getMessage());
                    return -1;
                }


            }
        }
        return -1;
    }
}
