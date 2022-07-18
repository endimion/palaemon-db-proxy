package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.EvacuationStatus;
import gr.uaegean.palaemondbproxy.model.PameasPerson;

import java.util.List;
import java.util.Optional;

public interface ElasticService {

    public Optional<PameasPerson> getPersonBySurname(String surname);
    public Optional<PameasPerson> getPersonByHashedMacAddress(String hashedMacAddress);
    public Optional<PameasPerson> getPersonByPersonalIdentifierDecrypted(String personalIdentifier);
    public List<PameasPerson> getAllPersonsDecrypted();

    public List<PameasPerson> getAllPassengersDecrypted();

    public void updatePerson(String personIdentifier, PameasPerson person);
    public void save(PameasPerson person);


    public Optional<EvacuationStatus> getEvacuationStatus();
    public void saveEvacuationStatus(EvacuationStatus evacuationStatus);

    public Optional<PameasPerson> getPersonByBraceletId(String braceletId);

}
