package gr.uaegean.palaemondbproxy.service.impl;

import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.model.TO.MinLocationTO;
import gr.uaegean.palaemondbproxy.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {
    static final String TOPIC = "pameas-person";
    static final String LOCATION_TOPIC = "pameas-location";

    private final KafkaProducer<String, PameasPerson> personProducer;
    private final KafkaProducer<String, MinLocationTO> locationProducer;


    @Autowired
    public KafkaServiceImpl(KafkaProducer<String, PameasPerson> producer, KafkaProducer<String, MinLocationTO> locationProducer) {
        this.personProducer = producer;
        this.locationProducer = locationProducer;
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
}
