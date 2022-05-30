package gr.uaegean.palaemondbproxy.config;

import gr.uaegean.palaemondbproxy.model.TO.EvacuationStatusTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StateManagementConfig {


    @Bean
    public EvacuationStatusTO getEvacuationStatus(){
        return  new EvacuationStatusTO();
    }
}
