package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.Geofence;
import gr.uaegean.palaemondbproxy.model.Incident;
import gr.uaegean.palaemondbproxy.model.TO.GeofenceTO;
import gr.uaegean.palaemondbproxy.model.TO.IncidentTO;
import gr.uaegean.palaemondbproxy.repository.IncidentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.sql.Wrapper;
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
    public @ResponseBody String updateIncidentStatus(@RequestBody IncidentTO submittedIncident) {
        Optional<Incident> incident = incidentRepository.findById(submittedIncident.getId());
        if (incident.isPresent()) {
            incident.get().setStatus(submittedIncident.getStatus());
            incidentRepository.save(incident.get());
            return "OK";
        } else {
            return "NOT_FOUND";
        }
    }


    @GetMapping("/getPassengerIncident")
    public @ResponseBody IncidentTO getPassengerIncident(@RequestParam String id) {
        Optional<Incident> incident = incidentRepository.findByIncidentId(id);
        IncidentTO result = new IncidentTO();
        if (incident.isPresent()) {
            result.setGeofence(incident.get().getGeofenceId());
            result.setPassengerName(incident.get().getPassengerName());
            result.setPassengerSurname(incident.get().getPassengerSurname());
            result.setDeck(incident.get().getDeck());
            result.setId(incident.get().getId());
            result.setHealthIssues(incident.get().getHealthIssues());
            result.setStatus(incident.get().getStatus());
            result.setTimestamp(incident.get().getTimestamp());
            result.setMobilityIssues(incident.get().getMobilityIssues());
            result.setAssignedCrewMemberId(incident.get().getAssignedCrewMemberIdDecrypted());
            result.setPregnancyStatus(incident.get().getPregnancyStatus());
            result.setXLoc(incident.get().getXLoc());
            result.setYLoc(incident.get().getYLoc());
            result.setPreferredLanguage(incident.get().getPreferredLanguage());
            result.setIncidentId(incident.get().getIncidentId());
            return result;
        }
        return null;
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
        incident.setIncidentId(submittedIncident.getIncidentId());
        return incident;
    }
}
