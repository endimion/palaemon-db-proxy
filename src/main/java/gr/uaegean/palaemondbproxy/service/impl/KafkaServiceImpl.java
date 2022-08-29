package gr.uaegean.palaemondbproxy.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uaegean.palaemondbproxy.model.Incident;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.*;
import gr.uaegean.palaemondbproxy.repository.IncidentRepository;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.service.KafkaService;
import gr.uaegean.palaemondbproxy.utils.CryptoUtils;
import gr.uaegean.palaemondbproxy.utils.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {
    static final String TOPIC = "pameas-person";
    static final String LOCATION_TOPIC = "pameas-location";

    private final KafkaProducer<String, PameasPerson> personProducer;
    private final KafkaProducer<String, MinLocationTO> locationProducer;

    private final KafkaProducer<String, PameasNotificationTO> notificationProducer;

    private final KafkaProducer<String, SrapTO> srapTOKafkaProducer;

    private final KafkaProducer<String, KafkaHeartBeatResponse> heartBeatProducer;

    private final KafkaProducer<String, EvacuationCoordinatorEventTO> evacuationCoordinatorProducer;


    @Autowired
    EvacuationStatusTO evacuationStatusTO;

    @Autowired
    ElasticService elasticService;

    @Autowired
    CryptoUtils cryptoUtils;

    @Autowired
    IncidentRepository incidentRepository;


    @Autowired
    public KafkaServiceImpl(KafkaProducer<String, PameasPerson> producer,
                            KafkaProducer<String, MinLocationTO> locationProducer,
                            KafkaProducer<String, PameasNotificationTO> notificationProducer,
                            KafkaProducer<String, KafkaHeartBeatResponse> heartBeatProducer,
                            KafkaProducer<String, SrapTO> srapTOKafkaProducer
            , KafkaProducer<String, EvacuationCoordinatorEventTO> evacuationCoordinatorProducer)

    {
        this.personProducer = producer;
        this.locationProducer = locationProducer;
        this.notificationProducer = notificationProducer;
        this.heartBeatProducer = heartBeatProducer;
        this.srapTOKafkaProducer = srapTOKafkaProducer;
        this.evacuationCoordinatorProducer =evacuationCoordinatorProducer;
    }

    @Override
    public void savePerson(PameasPerson person) {
        try {
//            log.info("pushing to  kafka {}", person);
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
//            log.info("pushing to  kafka {}", location);

            this.locationProducer.send(new ProducerRecord<>(LOCATION_TOPIC, location));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void writePameasNotification(PameasNotificationTO notification) {
        try {
            log.info("pushing to  kafka PAMEAS-NOTIFICATION {}", notification);
            this.notificationProducer.send(new ProducerRecord<>("pameas-notification", notification));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void writeSRAPTest(SrapTO srapTO) {
        try {
            log.info("pushing to  kafka srap {}", srapTO);
            this.srapTOKafkaProducer.send(new ProducerRecord<>("srap", srapTO));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void writeToEvacuationCoordinator(EvacuationCoordinatorEventTO eventTO) {
        try {
            log.info("pushing to  kafka {}", eventTO);
            this.evacuationCoordinatorProducer.send(new ProducerRecord<>("evacuation-coordinator", eventTO));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


//    @Override
//    @KafkaListener(topics = "evacuation-coordinator", groupId = "uaeg-consumer-group")
//    public void monitorEvacuationCoordinator(String message) {
//        log.info("message from evacuation-coordinator ${}", message);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        EvacuatorRequest request = null;
//        try {
//            request = mapper.readValue(message, EvacuatorRequest.class);
//            KafkaHeartBeatResponse response = new KafkaHeartBeatResponse();
//            response.setOriginator("PaMEAS-Location");
//            this.evacuationStatusTO.setStatus(request.getCurrent());
//            if (this.evacuationStatusTO.getStatus().equals("0")) {
//                response.setOperationMode("0");
//            } else {
//                response.setOperationMode("1");
//            }
//            response.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
//            this.heartBeatProducer.send(new ProducerRecord<>("evacuation-component-status", response));
//        } catch (JsonProcessingException e) {
//            log.error(e.getMessage());
//        }
//    }

//    @Override
//    @KafkaListener(topics = "resource-discovery-request", groupId = "uaeg-consumer-group")
//    public void monitorResourceDiscover(String message) {
//        log.info("message from resource-discovery-request ${}", message);
//        KafkaHeartBeatResponse response = new KafkaHeartBeatResponse();
//        sendHeartBeatResponse("resource-discovery-response");
//    }


//    @Override
//    @KafkaListener(topics = "heartbeat-request", groupId = "uaeg-consumer-group")
//    public void monitorHeartbeat(String message) {
//        log.info("message from heartbeat-request ${}", message);
//        sendHeartBeatResponse("heartbeat-response");
//    }
//
//
//    public void sendHeartBeatResponse(String topic) {
//        KafkaHeartBeatResponse response = new KafkaHeartBeatResponse();
//        if (!this.evacuationStatusTO.getStatus().equals("0")) {
//            response.setOperationMode("1");
//        } else {
//            response.setOperationMode("0");
//        }
//        response.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
//        this.heartBeatProducer.send(new ProducerRecord<>(topic, response));
//    }


//    @Override
//    @KafkaListener(topics = "smart-bracelet-sensor-data", groupId = "uaeg-consumer-group")
//    public void monitorBraceletSaturation(String message) {
//        log.info("message from /smart-bracelet-sensor-data ${}", message);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        try {
//            BraceletDataTO braceletDataTO = mapper.readValue(message, BraceletDataTO.class);
//            Optional<PameasPerson> person = elasticService.getPersonByBraceletId(braceletDataTO.getId());
//            if (person.isPresent()) {
//                person.get().getPersonalInfo().setOxygenSaturation(braceletDataTO.getSp02());
//                String decryptedPersonaId = this.cryptoUtils.decryptBase64Message(person.get().getPersonalInfo().getPersonalId());
//                elasticService.updatePerson(decryptedPersonaId, person.get());
//            } else {
//                log.error("no person found with bracelet {}", braceletDataTO.getId());
//            }
//        } catch (JsonProcessingException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException |
//                 IllegalBlockSizeException | NoSuchPaddingException e) {
//            log.error(e.getMessage());
//        }
//    }

//    @Override
//    @KafkaListener(topics = "smart-bracelet-event-notification", groupId = "uaeg-consumer-group")
//    public void monitorBraceletFallEvent(String message) {
//        log.info("message from /smart-bracelet-event-notification ${}", message);
//        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        try {
//            BraceletFallTO braceletFallTO = mapper.readValue(message, BraceletFallTO.class);
//            Optional<PameasPerson> person = elasticService.getPersonByBraceletId(braceletFallTO.getId());
//            if (person.isPresent()) {
//                PameasNotificationTO notificationTO = Wrappers.pameasPersonToNotificationTO(person.get());
//                this.writePameasNotification(notificationTO);
//
//                //finally update so that the person is displayed as fallen
//                person.get().getPersonalInfo().setHasFallen("true");
//                String decryptedPersonaId = this.cryptoUtils.decryptBase64Message(person.get().getPersonalInfo().getPersonalId());
//                elasticService.updatePerson(decryptedPersonaId, person.get());
//
//            }
//
//        } catch (JsonProcessingException | IllegalBlockSizeException | NoSuchPaddingException |
//                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
//            log.error(e.getMessage());
//        }
//    }

//    @Override
//    public void monitorSRAP(String message) {
//        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        try {
//            SrapTO srapTO = mapper.readValue(message, SrapTO.class);
//            if (!StringUtils.isEmpty(srapTO.getPassengerId())) {
//                //  status: “string” (assistance_required, movement_delayed, free_movement)
//                if (srapTO.getStatus().equals("assistance_required")) {
//                    Optional<PameasPerson> person = this.elasticService.getPersonByPersonalIdentifierDecrypted(srapTO.getPassengerId());
//                    if (person.isPresent()) {
//                        PameasNotificationTO notificationTO = Wrappers.pameasPersonToNotificationTO(person.get());
//                        this.writePameasNotification(notificationTO);
//                    }
//                }
//            }else{
//                if(!StringUtils.isEmpty(srapTO.getZoneId())){
//                    //TODO zoneId to geofence (info to be provided by NTUA)
//                    if(srapTO.getStatus().equals("closed")){
//                        //Zone was blocked!!
//                        PameasNotificationTO pameasNotificationTO = new PameasNotificationTO();
//                        //TODO this needs mapping
//                        pameasNotificationTO.setGeofence(srapTO.getZoneId());
//                        pameasNotificationTO.setStatus("blocked");
//                        pameasNotificationTO.setType("SRAP_BLOCKED_GEOFENCE");
//                        this.writePameasNotification(pameasNotificationTO);
//                    }
//                }
//            }
//
//
//        } catch (JsonProcessingException e) {
//            log.error("error parsing SRAP message {}", e.getMessage());
//        }
//    }


}
