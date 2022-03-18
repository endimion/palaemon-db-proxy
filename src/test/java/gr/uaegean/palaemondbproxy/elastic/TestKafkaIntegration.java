package gr.uaegean.palaemondbproxy.elastic;

import gr.uaegean.palaemondbproxy.model.*;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import gr.uaegean.palaemondbproxy.service.KafkaService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestKafkaIntegration {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private KafkaService kafkaService;


    @Test
    public void publishToKafka() {
        String topic = "pameas-person";
        try {
            this.producer.send(new ProducerRecord<String, String>(topic, "Message Value : " + Integer.toString(1)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }

    }

    @Test
    public void testKafkaService() {
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setGender("F");
        ticketInfo.setSurname("Triantafyllou2");
        ticketInfo.setName("Katerina");
        ticketInfo.setTicketNumber("123");

        PameasPerson p = new PameasPerson();
        Personalinfo pii = new Personalinfo();
        pii.setCrew(false);
        pii.setGender("M");
        pii.setDateOfBirth("1983-10-05");
        pii.setRole(null);
        pii.setMedicalCondition(null);
        pii.setName("Nikolaos");
        pii.setSurname("Triantafyllou2");
        pii.setTicketInfo(asList(ticketInfo));
        pii.setPersonalId("gr/gr/123");

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

        kafkaService.savePerson(p);

    }


}
