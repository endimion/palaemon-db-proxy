package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.model.TO.MinLocationTO;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import gr.uaegean.palaemondbproxy.service.KafkaService;
import gr.uaegean.palaemondbproxy.service.PersonService;
import gr.uaegean.palaemondbproxy.utils.LocationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LocationControllers {

    @Autowired
    KafkaService kafkaService;

    @Autowired
    PersonService personService;

    @PostMapping("/addLocation")
    public void pushLocation(@RequestBody LocationTO location){
        MinLocationTO minLocationTO = LocationUtils.reduceLocation(location);

        personService.addLocationToPerson(location);
        kafkaService.saveLocation(minLocationTO);
    }

    public UserLocationUnit getLocationOfPerson(){
        return null;
    }

    public UserGeofenceUnit getGeofenceOfPerson(){
        return null;
    }
}
