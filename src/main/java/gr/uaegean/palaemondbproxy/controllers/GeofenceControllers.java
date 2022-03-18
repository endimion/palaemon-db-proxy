package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.Geofence;
import gr.uaegean.palaemondbproxy.model.TO.GeofenceStatusResponse;
import gr.uaegean.palaemondbproxy.model.TO.GeofenceTO;
import gr.uaegean.palaemondbproxy.repository.GeofenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class GeofenceControllers {

    @Autowired
    GeofenceRepository geofenceRepository;


    @PostMapping("/updateGeofence")
    public @ResponseBody
    String updateGeofence(@RequestBody GeofenceTO submittedGeofence) {
        Optional<Geofence> existingGeofence = geofenceRepository.findById(submittedGeofence.getId());
        if (existingGeofence.isPresent()) {
            existingGeofence.get().setStatus(submittedGeofence.getStatus());
            existingGeofence.get().setDeck(submittedGeofence.getDeck());
            existingGeofence.get().setGfName(submittedGeofence.getGfName());
            geofenceRepository.save(existingGeofence.get());
        }
        return "OK";
    }

    @PostMapping("/addGeofence")
    public @ResponseBody
    String addGeofence(@RequestBody GeofenceTO submittedGeofence) {
        geofenceRepository.save(transformToGeofence(submittedGeofence));
        return "OK";
    }

    @GetMapping("/getGeofenceStatus")
    public @ResponseBody
    GeofenceStatusResponse getListOfGeofences() {
        GeofenceStatusResponse response = new GeofenceStatusResponse();
        response.setMustering(new ArrayList<>());
        response.setSimple(new ArrayList<>());

        geofenceRepository.findAll().forEach(geofence -> {
            if(geofence.isMusteringStation()){
                response.getMustering().add(transformToGeofenceTO(geofence));
            }else {
                response.getSimple().add(transformToGeofenceTO(geofence));
            }
        });
        return  response;
    }


    private Geofence transformToGeofence(GeofenceTO geofenceTO) {
        Geofence geofence = new Geofence();
        geofence.setStatus(geofenceTO.getStatus());
        geofence.setDeck(geofenceTO.getDeck());
        geofence.setGfName(geofenceTO.getGfName());
        if (!StringUtils.hasLength(geofenceTO.getId())) {
            geofence.setId(UUID.randomUUID().toString());
        } else {
            geofence.setId(geofenceTO.getId());
        }
        geofence.setMusteringStation(geofenceTO.isMusteringStation());
        return geofence;
    }

    private GeofenceTO transformToGeofenceTO(Geofence geofence) {
        GeofenceTO geofenceTO = new GeofenceTO();
        geofenceTO.setStatus(geofence.getStatus());
        geofenceTO.setDeck(geofence.getDeck());
        geofenceTO.setGfName(geofence.getGfName());
        geofenceTO.setMusteringStation(geofence.isMusteringStation());
        geofenceTO.setId(geofence.getId());
        return geofenceTO;
    }

}
