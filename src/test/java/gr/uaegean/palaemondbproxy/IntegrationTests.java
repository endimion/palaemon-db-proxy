package gr.uaegean.palaemondbproxy;

import gr.uaegean.palaemondbproxy.model.TO.LocationHealthTO;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocation;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import gr.uaegean.palaemondbproxy.service.PersonService;
import gr.uaegean.palaemondbproxy.utils.EnvUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootTest
public class IntegrationTests {

    @Autowired
    PersonService personService;

    static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhLXRjZGlUVzBCNGxoR3liY1NsSzRPS0JBNUpUVWhvbGJsd0NtcFFxdU1JIn0.eyJqdGkiOiI3ZTM5ZmZlNi05ZDQ0LTQ4Y2MtOWY0MS01ZjkzMTVjZTYxNjMiLCJleHAiOjE2NDgxMjA2MjIsIm5iZiI6MCwiaWF0IjoxNjQ4MTIwMzIyLCJpc3MiOiJodHRwczovL2RzczEuYWVnZWFuLmdyL2F1dGgvcmVhbG1zL3BhbGFlbW9uIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjQxNjYwOGVjLTgzYjQtNDE0Mi05ZDExLWU5MTUxYjUxY2NhMCIsInR5cCI6IkJlYXJlciIsImF6cCI6InBhbGFlbW9uUmVnaXN0cmF0aW9uIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNDcyNjIwNTctNzRmYi00NmNkLTllOTEtOTdiNmFiZTdlNDA4IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImNsaWVudEhvc3QiOiIxOTUuMjUxLjE0MS41MyIsImNsaWVudElkIjoicGFsYWVtb25SZWdpc3RyYXRpb24iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1wYWxhZW1vbnJlZ2lzdHJhdGlvbiIsImNsaWVudEFkZHJlc3MiOiIxOTUuMjUxLjE0MS41MyIsImVtYWlsIjoic2VydmljZS1hY2NvdW50LXBhbGFlbW9ucmVnaXN0cmF0aW9uQHBsYWNlaG9sZGVyLm9yZyJ9.HPII1cwJsPjWWOthKEK2G1JDnawDDR4lMsDZC0DGkeEQFEdIzwkd6LygI3r2N8v1Zzz6hBbNg5bt_-GHOQmtlC6oNgjPivya266S3ZyrqFlZTtPLsL0C5NXoUvyOPUngKo-7efGGv-WlT9wpr6YdXuOyEew6IId9SmIXLI2z2SAEZjpjRf9ccU9K84owD6xtEe43HeqW6YHh99fu5lkj7j74zApezCvPdJ_Eemm0JpeCQtmn0-TU6F-OyvXJC9IwCDjeSmV5V6WrEkU2IamJtfjMBm1FjRxi65S1p8ziAJ17CxKLFZa_CWG6niPP4ZXNZzg6UHsh2sJWxPM9SSJDng";

    @Test
    public void testAddPerson() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://dss.aegean.gr:8090/addPerson/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .method("POST", HttpRequest.BodyPublishers.ofString("{\n    \"name\" : \"CLAUDE\",\n    \"surname\": \"PHIL\",\n    \"identifier\": \"EL/EL/11111\",\n    \"gender\": \"'Male'\",\n    \"age\": \"'1965-01-01'\",\n\t\"preferred_language\":[\"EL\", \"ES\"],\n    \"ticketNumber\": \"'123'\",\n    \"medical_condnitions\": \"'non'\",\n\t\"isCrew\": \"true\",\n\t\"role\": \"captain\"\n  }"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }



//    @Test
    public void testKafkaRead() {
        Properties properties = new Properties();
        String trustStoreLocation = EnvUtils.getEnvVar("KAFKA_TRUST_STORE_LOCATION", "/home/ni/code/java/palaemon-db-proxy/truststore.jks");
        String trustStorePass = EnvUtils.getEnvVar("KAFKA_TRUST_STORE_PASSWORD", "teststore");
        String keyStoreLocation = EnvUtils.getEnvVar("KAFKA_KEYSTORE_LOCATION", "/home/ni/code/java/palaemon-db-proxy/keystore.jks");
        String keyStorePass = EnvUtils.getEnvVar("KAFKA_KEY_STORE_PASSWORD", "teststore");
        String kafkaURI = EnvUtils.getEnvVar("KAFKA_URI_WITH_PORT","dfb.palaemon.itml.gr:30093");

        properties.put("bootstrap.servers", kafkaURI);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("security.protocol", "SSL");
        properties.put("ssl.truststore.location", trustStoreLocation);
        properties.put("ssl.truststore.password", trustStorePass);
        properties.put("ssl.keystore.location", keyStoreLocation);
        properties.put("ssl.keystore.password", keyStorePass);

        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "uaeg-consumer-group");
        KafkaConsumer<String, String> consumer = null;
        try {
            ArrayList<String> topics = new ArrayList<String>();
//            topics.add("test-poc");
//            topics.add("poc-time");
//            topics.add("smart-bracelet-event-notification");
//            topics.add("smart-bracelet-sensor-data");
//            topics.add("smart-bracelet-pameas-evac-msg");
//            topics.add("srap");
            topics.add("pameas-notification");
//            topics.add("pameas-location");
            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(topics);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    String recordString = record.toString();
                    System.out.println("Record read in KafkaConsumerApp : " + record.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Something bad happened : ");
            e.printStackTrace();
        } finally {
            consumer.close();
        }


    }


    @Test
    public void addLocationHealthToPerson(){
        LocationHealthTO locationHealthTO = new LocationHealthTO();
        UserLocationUnit userLocationUnit = new UserLocationUnit();
        userLocationUnit.setYLocation("27.497502709677637");
        userLocationUnit.setXLocation("91.91315958190962");
        userLocationUnit.setTimestamp((new Timestamp(System.currentTimeMillis())).toString());
        userLocationUnit.setHashedMacAddress("b356d0ea840b0550a2acb26acea468a80b895607d55db030f0b12abf5e8ce759");
        userLocationUnit.setGeofenceNames(List.of(new String[]{"geoName"}));
        userLocationUnit.setBuildingId("bid");
        userLocationUnit.setCampusId("cid");




        UserGeofenceUnit userGeofenceUnit = new UserGeofenceUnit();
        userGeofenceUnit.setDeck("7");
        userGeofenceUnit.setHashedMacAddress("b356d0ea840b0550a2acb26acea468a80b895607d55db030f0b12abf5e8ce759");
        userGeofenceUnit.setDwellTime("1");
        userGeofenceUnit.setMacAddress("58:37:8B:DE:42:F7");
        userGeofenceUnit.setGfEvent("1");
        userGeofenceUnit.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        userGeofenceUnit.setDwellTime("2");
        userGeofenceUnit.setGfName("ge0");


        locationHealthTO.setGeofence(userGeofenceUnit);
        locationHealthTO.setLocation(userLocationUnit);
        locationHealthTO.setMacAddress("58:37:8B:DE:42:F7");
        locationHealthTO.setHashedMacAddress("b356d0ea840b0550a2acb26acea468a80b895607d55db030f0b12abf5e8ce759");
        locationHealthTO.setSaturation("70");
        locationHealthTO.setHeartBeat("80");

        this.personService.addLocationHealthToPerson(locationHealthTO);


    }

}
