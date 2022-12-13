package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.*;

import java.util.List;

public interface KafkaService {

    public void savePerson(PameasPerson person);

    public void saveLocation(MinLocationTO location);

    public void writeSRAPLocation(SRAPLocationTO location);


    public void writePameasNotification(PameasNotificationTO notification);


    public void writeSRAPTest(SrapTO srapTO);

    public void writeToEvacuationCoordinator(EvacuationCoordinatorEventTO eventTO);




}
