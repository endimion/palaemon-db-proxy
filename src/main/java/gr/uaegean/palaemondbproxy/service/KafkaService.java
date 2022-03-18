package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.model.TO.MinLocationTO;

import java.util.List;

public interface KafkaService {

    public void savePerson(PameasPerson person);

    public void saveLocation(MinLocationTO location);

}
