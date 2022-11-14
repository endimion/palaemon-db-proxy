package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.EvacuationStatus;
import gr.uaegean.palaemondbproxy.model.Geofence;
import gr.uaegean.palaemondbproxy.model.PameasPerson;

import java.util.List;
import java.util.Optional;

public interface ElasticService {

    public Optional<PameasPerson> getPersonBySurname(String surname);
    public Optional<PameasPerson> getPersonByHashedMacAddress(String hashedMacAddress);
    public Optional<PameasPerson> getPersonByMacAddress(String macAddress);

    public Optional<PameasPerson> getPersonByTicketNumber(String ticketNumber);

    public Optional<PameasPerson> getPersonByMumbleName(String mumbleName);
    public Optional<PameasPerson> getPersonByPersonalIdentifierDecrypted(String personalIdentifier);
    public List<PameasPerson> getAllPersonsDecrypted();

    public List<PameasPerson> getAllPassengersDecrypted();

    public List<PameasPerson> getAllCrewMembersDecrypted();

    public void updatePerson(String personIdentifierDecrypted, PameasPerson person);
    public void save(PameasPerson person);


    public Optional<EvacuationStatus> getEvacuationStatus();
    public void saveEvacuationStatus(EvacuationStatus evacuationStatus);

    public Optional<PameasPerson> getPersonByBraceletId(String braceletId);


    public Optional<Geofence> getGeofenceByName(String name);

    public List<Geofence> getGeofences();



}
