package gr.uaegean.palaemondbproxy.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.*;
import gr.uaegean.palaemondbproxy.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {
    static final String TOPIC = "pameas-person";
    static final String LOCATION_TOPIC = "pameas-location";

    private final KafkaProducer<String, PameasPerson> personProducer;
    private final KafkaProducer<String, MinLocationTO> locationProducer;

    private final KafkaProducer<String, PameasNotificationTO> notificationProducer;

    private final KafkaProducer<String, KafkaHeartBeatResponse> heartBeatProducer;


    @Autowired
    EvacuationStatusTO evacuationStatusTO;

    @Autowired
    public KafkaServiceImpl(KafkaProducer<String, PameasPerson> producer,
                            KafkaProducer<String, MinLocationTO> locationProducer,
                            KafkaProducer<String, PameasNotificationTO> notificationProducer,
                            KafkaProducer<String, KafkaHeartBeatResponse> heartBeatProducer) {
        this.personProducer = producer;
        this.locationProducer = locationProducer;
        this.notificationProducer = notificationProducer;
        this.heartBeatProducer = heartBeatProducer;
    }

    @Override
    public void savePerson(PameasPerson person) {
        try {
            log.info("pushing to  kafka {}", person);
            this.personProducer.send(new ProducerRecord<>(TOPIC, person));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
//            producer.close();
        }
    }

    @Override
    public void saveLocation(MinLocationTO location) {
        try {
            log.info("pushing to  kafka {}", location);

            this.locationProducer.send(new ProducerRecord<>(LOCATION_TOPIC, location));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void writePameasNotification(PameasNotificationTO notification) {
        try {
            log.info("pushing to  kafka {}", notification);
            this.notificationProducer.send(new ProducerRecord<>("pameas-notification", notification));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Override
    @KafkaListener(topics = "evacuation-coordinator", groupId = "uaeg-consumer-group")
    public void monitorEvacuationCoordinator(String message) {
        log.info("message from evacuation-coordinator ${}", message);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        EvacuatorRequest request = null;
        try {
            request = mapper.readValue(message, EvacuatorRequest.class);
            KafkaHeartBeatResponse response = new KafkaHeartBeatResponse();
            response.setOriginator("PaMEAS-Location");
            this.evacuationStatusTO.setStatus(request.getCurrent());
            if (this.evacuationStatusTO.getStatus().equals("0")) {
                response.setOperationMode("0");
            }else{
                response.setOperationMode("1");
            }
            response.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
            this.heartBeatProducer.send(new ProducerRecord<>("evacuation-component-status", response));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @KafkaListener(topics = "resource-discovery-request", groupId = "uaeg-consumer-group")
    public void monitorResourceDiscover(String message) {
        log.info("message from resource-discovery-request ${}", message);
        KafkaHeartBeatResponse response = new KafkaHeartBeatResponse();
        sendHeartBeatResponse("resource-discovery-response");
    }

    @Override
    @KafkaListener(topics = "heartbeat-request", groupId = "uaeg-consumer-group")
    public void monitorHeartbeat(String message) {
        log.info("message from heartbeat-request ${}", message);
        sendHeartBeatResponse("heartbeat-response");
    }


    public void sendHeartBeatResponse(String topic) {
        KafkaHeartBeatResponse response = new KafkaHeartBeatResponse();
        if (!this.evacuationStatusTO.getStatus().equals("0")) {
            response.setOperationMode("1");
        }else{
            response.setOperationMode("0");
        }
        response.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        this.heartBeatProducer.send(new ProducerRecord<>(topic, response));
    }


}
