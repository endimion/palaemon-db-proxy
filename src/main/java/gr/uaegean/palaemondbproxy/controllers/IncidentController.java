package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.Geofence;
import gr.uaegean.palaemondbproxy.model.Incident;
import gr.uaegean.palaemondbproxy.model.TO.GeofenceTO;
import gr.uaegean.palaemondbproxy.model.TO.IncidentTO;
import gr.uaegean.palaemondbproxy.repository.IncidentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class IncidentController {

    @Autowired
    IncidentRepository incidentRepository;

    @PostMapping("/declarePassengerIncident")
    public @ResponseBody
    String declarePassengerIncident(@RequestBody IncidentTO submittedIncident) {
        Incident incident = transformIncidentTO2Incident(submittedIncident);
        incidentRepository.save(incident);
        return "OK";
    }

    @PostMapping("/updatePassengerIncident")
    public @ResponseBody String updateIncidentStatus(@RequestBody IncidentTO submittedIncident ){
        Optional<Incident> incident = incidentRepository.findById(submittedIncident.getId());
        if(incident.isPresent()){
            incident.get().setStatus(submittedIncident.getStatus());
            incidentRepository.save(incident.get());
            return  "OK";
        }else{
            return  "NOT_FOUND";
        }
    }


    public Incident transformIncidentTO2Incident(IncidentTO submittedIncident) {
        Incident incident = new Incident();
        incident.setPassengerSurname(submittedIncident.getPassengerSurname());
        incident.setPassengerName(submittedIncident.getPassengerName());
        incident.setPregnancyStatus(submittedIncident.getPregnancyStatus());
        incident.setStatus(submittedIncident.getStatus());
        incident.setXLoc(submittedIncident.getXLoc());
        incident.setYLoc(submittedIncident.getYLoc());
        incident.setDeck(submittedIncident.getDeck());
        incident.setMobilityIssues(submittedIncident.getMobilityIssues());
        incident.setHealthIssues(submittedIncident.getHealthIssues());
        Date date = new Date();
        incident.setTimestamp((new Timestamp(date.getTime())).toString());
        incident.setId(UUID.randomUUID().toString());
        incident.setPreferredLanguage(submittedIncident.getPreferredLanguage());
        incident.setAssignedCrewMemberIdDecrypted(submittedIncident.getAssignedCrewMemberId());
        incident.setGeofenceId(submittedIncident.getGeofence());
        incident.setStatus(Incident.IncidentStatus.OPEN);
        return incident;
    }
}
