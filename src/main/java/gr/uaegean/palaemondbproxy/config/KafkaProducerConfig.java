package gr.uaegean.palaemondbproxy.config;

import gr.uaegean.palaemondbproxy.utils.EnvUtils;
import gr.uaegean.palaemondbproxy.utils.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaProducerConfig {
    //TODO read these from .env
    private final Properties properties = new Properties();


    public KafkaProducerConfig() {
        String kafkaURI = EnvUtils.getEnvVar("KAFKA_URI_WITH_PORT","dfb.palaemon.itml.gr:30093");

//                StringUtils.isEmpty(System.getenv("KAFKA_URI"))?"dfb.palaemon.itml.gr:30093":System.getenv("KAFKA_URI");
        String trustStoreLocation = EnvUtils.getEnvVar("KAFKA_TRUST_STORE_LOCATION", "/home/ni/code/java/palaemon-db-proxy/truststore.jks");
//                StringUtils.isEmpty(System.getenv("TRUST_STORE_LOCATION"))?
//                "/home/ni/code/java/palaemon-db-proxy/truststore.jks":System.getenv("TRUST_STORE_LOCATION");
        String trustStorePass = EnvUtils.getEnvVar("KAFKA_TRUST_STORE_PASSWORD", "teststore");
//                StringUtils.isEmpty(System.getenv("TRUST_STORE_PASSWORD"))?
//                "teststore":System.getenv("TRUST_STORE_PASSWORD");


        String keyStoreLocation = EnvUtils.getEnvVar("KAFKA_KEYSTORE_LOCATION", "/home/ni/code/java/palaemon-db-proxy/keystore.jks");
//                StringUtils.isEmpty(System.getenv("KEY_STORE_LOCATION"))?
//                "/home/ni/code/java/palaemon-db-proxy/keystore.jks":System.getenv("KEY_STORE_LOCATION");
        String keyStorePass = EnvUtils.getEnvVar("KAFKA_KEY_STORE_PASSWORD", "teststore");
//                StringUtils.isEmpty(System.getenv("KEY_STORE_PASSWORD"))?
//                "teststore":System.getenv("KEY_STORE_PASSWORD");

        this.properties.put("bootstrap.servers", kafkaURI);
        this.properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.properties.put("security.protocol", "SSL");
        this.properties.put("ssl.truststore.location", trustStoreLocation);
        this.properties.put("ssl.truststore.password", trustStorePass);
        this.properties.put("ssl.keystore.location", keyStoreLocation);
        this.properties.put("ssl.keystore.password", keyStorePass);
    }

    @Bean
    public KafkaProducer producer() {
        KafkaProducer<String,String> myProducer= new KafkaProducer<String,String>(this.properties,new StringSerializer(),
                new KafkaJsonSerializer());
        return myProducer;
    }


}
