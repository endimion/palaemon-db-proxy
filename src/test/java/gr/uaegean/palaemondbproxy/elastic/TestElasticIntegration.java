package gr.uaegean.palaemondbproxy.elastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uaegean.palaemondbproxy.model.*;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import gr.uaegean.palaemondbproxy.repository.GeofenceRepository;
import gr.uaegean.palaemondbproxy.repository.IncidentRepository;
import gr.uaegean.palaemondbproxy.repository.PameasPersonRepository;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.utils.CryptoUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestElasticIntegration {

    @Autowired
    ElasticsearchOperations elasticsearchTemplate;

    @Autowired
    PameasPersonRepository personRepository;

    @Autowired
    ElasticService elasticService;

    @Autowired
    GeofenceRepository geofenceRepository;

    @Autowired
    IncidentRepository incidentRepository;

    @Autowired
    CryptoUtils cryptoUtils;


    @Test
    public void addPameasPersonPassenger() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, JsonProcessingException {
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setGender("F");
        ticketInfo.setSurname(cryptoUtils.encryptBase64("Triantafyllou"));
        ticketInfo.setName(cryptoUtils.encryptBase64("Katerina"));
        ticketInfo.setTicketNumber("123");

        PameasPerson p = new PameasPerson();
        Personalinfo pii = new Personalinfo();
        pii.setCrew(false);
        pii.setGender("M");
        pii.setDateOfBirth("1965-01-01");
        pii.setRole("passenger");
        pii.setMedicalCondition(null);
        pii.setName(cryptoUtils.encryptBase64("myNameisPassenger"));
        pii.setSurname(cryptoUtils.encryptBase64("TriantafyllouPassenger"));
        pii.setTicketInfo(asList(ticketInfo));
        pii.setPersonalId(cryptoUtils.encryptBase64("gr/gr/123"));
        pii.setAssignedMusteringStation("geofence3");
        pii.setInPosition(true);

        pii.setPreferredLanguage(Arrays.asList(new String[]{"en", "gr"}));

        p.setPersonalInfo(pii);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setImei("1231imei");
        deviceInfo.setImsi("123imsi");
        deviceInfo.setMacAddress(cryptoUtils.encryptBase64("AbADC2123::123casdAX"));
        deviceInfo.setMsisdn("123msisdn");

        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setDeviceInfoList(asList(deviceInfo));

        p.setNetworkInfo(networkInfo);

        LocationInfo locationInfo = new LocationInfo();
        UserLocationUnit l = new UserLocationUnit();
        l.setBuildingId("b1");
        l.setCampusId("c1");
        l.setErrorLevel("0");
        l.setFloorId("f1");
        l.setGeofenceId("g1");
        l.setTimestamp("1231321321");
        l.setHashedMacAddress("12321xadfas21");
        l.setIsAssociated("test");
        l.setXLocation("x");
        l.setYLocation("y");
        l.setGeofenceNames(asList("geofence name 1"));

        locationInfo.setLocationHistory(asList(l));
        UserGeofenceUnit g = new UserGeofenceUnit();
        g.setDwellTime("1231231");
        g.setIsAssociated("test");
        g.setMacAddress("mac::123xasdf::12 ");
        g.setGfName("geofence name 1");
        g.setTimestamp("123321321");
        g.setGfEvent("event");
        g.setGfId("id");

        locationInfo.setGeofenceHistory(asList(g));

        p.setLocationInfo(locationInfo);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(p));

        personRepository.save(p);

    }

    @Test
    public void testUpdatePerson() {
        String surname = "Triantafyllou3";
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("personalInfo.surname", surname).minimumShouldMatch("100%"))
                .build();
        SearchHits<PameasPerson> matchingPersons =
                elasticsearchTemplate.search(searchQuery, PameasPerson.class, IndexCoordinates.of("pameas-person-2021.12.08"));
        if (matchingPersons.getTotalHits() > 0) {
            PameasPerson person = matchingPersons.getSearchHit(0).getContent();
            person.getPersonalInfo().setName("Nikolas");
            personRepository.save(person);
        }
    }

    @Test
    public void testUpdatePersonService() {
        PameasPerson person = elasticService.getPersonBySurname("Triantafyllou").get();
        person.getPersonalInfo().setName("Nikolakos");
        elasticService.updatePerson(person.getId(), person);
        assertEquals(elasticService.getPersonBySurname("Triantafyllou").get().getPersonalInfo().getName(), "Nikolakos");
    }


    @Test
    public void testSaveGeofence() {
        Geofence geofence = new Geofence();
        geofence.setId("id");
        geofence.setGfName("test_geofence_1");
        geofence.setDeck("deck_1");
        geofence.setMusteringStation(true);
        geofence.setStatus(Geofence.GeofenceStatusEnum.BLOCKED);

        geofenceRepository.save(geofence);
    }


    @Test
    public void addCrewMember() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {


        PameasPerson p = new PameasPerson();
        Personalinfo pii = new Personalinfo();
        pii.setCrew(true);
        pii.setGender("M");
        pii.setDateOfBirth("1965-01-01");
        pii.setRole("Captain");
        pii.setName("Nikolaos");
        pii.setSurname("Triantafyllou3");
        pii.setPersonalId(cryptoUtils.encryptBase64("gr/gr/123"));

        pii.setEmergencyDuty("Firefighting");
        LocalDateTime dutyStart = LocalDateTime.now();
        LocalDateTime dutyEnd = LocalDateTime.now().plusHours(8);
        DutySchedule schedule = new DutySchedule();
        schedule.setDutyStartDateTime(dutyStart);
        schedule.setDutyEndDateTime(dutyEnd);

        List<DutySchedule> crewMemberDuties = new ArrayList<>();
        crewMemberDuties.add(schedule);
        pii.setDutyScheduleList(crewMemberDuties);

        pii.setAssignmentStatus(Personalinfo.AssignmentStatus.UNASSIGNED);
        pii.setAssignedMusteringStation("geofence1");
        pii.setInPosition(true);


        p.setPersonalInfo(pii);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setImei("1231imei");
        deviceInfo.setImsi("123imsi");
        deviceInfo.setMacAddress("AbADC2123::123casdAX");
        deviceInfo.setMsisdn("123msisdn");

        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setDeviceInfoList(asList(deviceInfo));

        p.setNetworkInfo(networkInfo);

        LocationInfo locationInfo = new LocationInfo();
        UserLocationUnit l = new UserLocationUnit();
        l.setBuildingId("b1");
        l.setCampusId("c1");
        l.setErrorLevel("0");
        l.setFloorId("f1");
        l.setGeofenceId("g1");
        l.setTimestamp("1231321321");
        l.setHashedMacAddress("12321xadfas21");
        l.setIsAssociated("test");
        l.setXLocation("x");
        l.setYLocation("y");
        l.setGeofenceNames(asList("geofence name 1"));

        locationInfo.setLocationHistory(asList(l));
        UserGeofenceUnit g = new UserGeofenceUnit();
        g.setDwellTime("1231231");
        g.setIsAssociated("test");
        g.setMacAddress("mac::123xasdf::12 ");
        g.setGfName("geofence name 1");
        g.setTimestamp("123321321");
        g.setGfEvent("event");
        g.setGfId("id");

        locationInfo.setGeofenceHistory(asList(g));

        p.setLocationInfo(locationInfo);
        personRepository.save(p);

    }


    @Test
    public void createIncident() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Incident incident = new Incident();
        incident.setDeck("deck_1");
        incident.setGeofenceId("id");
        incident.setXLoc("123");
        incident.setYLoc("234");
        incident.setId("1");
        incident.setStatus(Incident.IncidentStatus.OPEN);
        incident.setTimestamp("123123");
        incident.setPreferredLanguage(new String[]{"en"});
        incident.setHealthIssues(null);
        incident.setMobilityIssues(null);
        incident.setAssignedCrewMemberIdDecrypted("gr/gr/123");
        incident.setPassengerName("passName1");
        incident.setPassengerSurname("passengerSurname1");
        incident.setPregnancyStatus("none");

        incidentRepository.save(incident);

    }


    @Test
    public void createEvacStatus(){
        EvacuationStatus status = new EvacuationStatus();
        status.setStatus("3");
        elasticService.saveEvacuationStatus(status);

    }

}
