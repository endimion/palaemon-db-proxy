package gr.uaegean.palaemondbproxy.utils;


import gr.uaegean.palaemondbproxy.model.TO.IncidentTO;
import gr.uaegean.palaemondbproxy.model.TO.NotificationIncidentTO;

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

}
