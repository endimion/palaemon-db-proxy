package gr.uaegean.palaemondbproxy.utils;


import gr.uaegean.palaemondbproxy.model.Incident;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.IncidentTO;
import gr.uaegean.palaemondbproxy.model.TO.NotificationIncidentTO;
import gr.uaegean.palaemondbproxy.model.TO.PameasNotificationTO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Wrappers {

    public static NotificationIncidentTO notificationTo2NotificationIncidientTO(IncidentTO incidentTO) {
        NotificationIncidentTO result = new NotificationIncidentTO();
        result.setHealthIssues(incidentTO.getHealthIssues());
        result.setStatus(incidentTO.getStatus().toString());
        result.setPassengerName(incidentTO.getPassengerName());
        result.setPassengerSurname(incidentTO.getPassengerSurname());
        result.setGeofence(incidentTO.getGeofence());
        result.setXloc(incidentTO.getXLoc());
        result.setId(incidentTO.getId());
        result.setPregnancyStatus(incidentTO.getPregnancyStatus());
        result.setYloc(incidentTO.getYLoc());
        result.setTimestamp(incidentTO.getTimestamp());
        result.setMobilityIssues(incidentTO.getMobilityIssues());
        result.setDeck(incidentTO.getDeck());
        return result;

    }


    public static PameasNotificationTO pameasPersonToNotificationTO(PameasPerson person) {

        IncidentTO incident = new IncidentTO();
        incident.setPassengerSurname(person.getPersonalInfo().getSurname());
        incident.setPassengerName(person.getPersonalInfo().getName());
        incident.setPregnancyStatus(person.getPersonalInfo().getPrengencyData());
        incident.setStatus(Incident.IncidentStatus.OPEN);
        incident.setXLoc(person.getLocationInfo().getLocationHistory().get(person.getLocationInfo().getLocationHistory().size() - 1).getXLocation());
        incident.setYLoc(person.getLocationInfo().getLocationHistory().get(person.getLocationInfo().getLocationHistory().size() - 1).getYLocation());
        incident.setDeck(person.getLocationInfo().getGeofenceHistory().get(person.getLocationInfo().getGeofenceHistory().size() - 1).getDeck());
        incident.setMobilityIssues(person.getPersonalInfo().getMobilityIssues());
        incident.setHealthIssues(person.getPersonalInfo().getMedicalCondition());
        Date date = new Date();
        incident.setTimestamp((new Timestamp(date.getTime())).toString());
        incident.setId(UUID.randomUUID().toString());
        int size = person.getPersonalInfo().getPreferredLanguage().size();
        String[] languages = new String[size];
        incident.setPreferredLanguage(person.getPersonalInfo().getPreferredLanguage().toArray(languages));
        incident.setGeofence(person.getLocationInfo().getGeofenceHistory().get(person.getLocationInfo().getGeofenceHistory().size() - 1).getGfName());
        incident.setIncidentId(incident.getId());


        NotificationIncidentTO notificationIncidentTO = Wrappers.notificationTo2NotificationIncidientTO(incident);
        PameasNotificationTO pameasNotificationTO = new PameasNotificationTO();
        pameasNotificationTO.setType("PASSENGER_ISSUE");
        pameasNotificationTO.setHealthIssues(incident.getHealthIssues());
        pameasNotificationTO.setTimestamp(incident.getTimestamp());
        pameasNotificationTO.setId(incident.getId());
        pameasNotificationTO.setPassengerName(incident.getPassengerName());
        pameasNotificationTO.setPassengerSurname(incident.getPassengerSurname());
        pameasNotificationTO.setStatus(incident.getStatus().toString());
        pameasNotificationTO.setMobilityIssues(notificationIncidentTO.getMobilityIssues());
        pameasNotificationTO.setAssignedCrewMemberId(null);
        pameasNotificationTO.setCrew(null);
        pameasNotificationTO.setGeofence(incident.getGeofence());
        pameasNotificationTO.setPregnancyStatus(incident.getPregnancyStatus());
        pameasNotificationTO.setPreferredLanguage(incident.getPreferredLanguage());
        pameasNotificationTO.setPreferredLanguage(incident.getPreferredLanguage());
        pameasNotificationTO.setMacAddress(person.getNetworkInfo().getDeviceInfoList().get(0).getHashedMacAddress());
        pameasNotificationTO.setIncident(notificationIncidentTO);
        pameasNotificationTO.setXloc(incident.getXLoc());
        pameasNotificationTO.setYloc(incident.getYLoc());
        pameasNotificationTO.setStatus(incident.getStatus().toString());
        pameasNotificationTO.setPreferredLanguage(incident.getPreferredLanguage());


        return pameasNotificationTO;


    }

}
