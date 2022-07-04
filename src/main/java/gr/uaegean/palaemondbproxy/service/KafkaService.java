package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.model.TO.MinLocationTO;
import gr.uaegean.palaemondbproxy.model.TO.NotificationIncidentCrewTO;
import gr.uaegean.palaemondbproxy.model.TO.PameasNotificationTO;

import java.util.List;

public interface KafkaService {

    public void savePerson(PameasPerson person);

    public void saveLocation(MinLocationTO location);

    public void writePameasNotification(PameasNotificationTO notification);

    public void monitorEvacuationCoordinator(String message);
    public void monitorHeartbeat(String message);

    public void monitorResourceDiscover(String message);

    public void monitorBraceletSaturation(String message);

    public void monitorBraceletFallEvent(String message);


}
