package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.EvacuationStatus;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationHealthTO;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.model.TO.MinLocationTO;
import gr.uaegean.palaemondbproxy.model.TO.PameasNotificationTO;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.service.KafkaService;
import gr.uaegean.palaemondbproxy.service.PersonService;
import gr.uaegean.palaemondbproxy.service.SpeedService;
import gr.uaegean.palaemondbproxy.utils.LocationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class LocationControllers {

    @Autowired
    KafkaService kafkaService;

    @Autowired
    PersonService personService;

    @Autowired
    ElasticService elasticService;

    @PostMapping("/addLocation")
    public void pushLocation(@RequestBody LocationTO location) {
        MinLocationTO minLocationTO = LocationUtils.reduceLocation(location);
        //if the passenger is already at the muster station
        // and we are at the mustering state (not at the Embarkation)
        // then generate a PaMEASNotificaiton with the PassengerId  and MusterSTation (geofence)
        // to alert the passenger
        Optional<PameasPerson> person = this.elasticService.getPersonByHashedMacAddress(location.getHashedMacAddress());
        Optional<EvacuationStatus> estatus = this.elasticService.getEvacuationStatus();
        if (!estatus.isPresent() || !estatus.get().getStatus().contains("7")) {
            //we are not in embarkation check if passengers is moving away from MS
            if (person.isPresent()) {
                int locationSize = person.get().getLocationInfo().getGeofenceHistory().size();
                //if the person was at the MS
                String ms = person.get().getPersonalInfo().getAssignedMusteringStation();
                if (ms != null && person.get().getLocationInfo() != null &&
                        person.get().getLocationInfo().getGeofenceHistory() != null
                        &&
                        person.get().getLocationInfo().getGeofenceHistory().size() > 0
                        &&
                        person.get().getLocationInfo().getGeofenceHistory().get(locationSize - 1).
                                getGfName().equals(ms)) {
                    //check if person is leaving the MS
                    if (!ms.equals(location.getGeofence().getGfName()) && !person.get().getPersonalInfo().getRole().equals("crew")) {
                        //Person left the MS!!
                        PameasNotificationTO pameasNotificationTO = new PameasNotificationTO();
                        pameasNotificationTO.setType("PASSENGER_EXITING_MS");
                        pameasNotificationTO.setMacAddress(location.getMacAddress());
                        kafkaService.writePameasNotification(pameasNotificationTO);
                    }
                }
            }

        }

        personService.addLocationToPerson(location);
        kafkaService.saveLocation(minLocationTO);
    }

    @PostMapping("/addLocationAndHealth")
    public void addLocationAndHealth(@RequestBody LocationHealthTO locationHealth) {
        LocationTO location = new LocationTO();
        location.setLocation(locationHealth.getLocation());
        location.setGeofence(locationHealth.getGeofence());
        location.setHashedMacAddress(locationHealth.getHashedMacAddress());
        location.setMacAddress(locationHealth.getMacAddress());

        MinLocationTO minLocationTO = LocationUtils.reduceLocation(location);

        personService.addLocationHealthToPerson(locationHealth);
        kafkaService.saveLocation(minLocationTO);
    }


    public UserLocationUnit getLocationOfPerson() {
        return null;
    }

    public UserGeofenceUnit getGeofenceOfPerson() {
        return null;
    }
}
