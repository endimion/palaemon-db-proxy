package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KafkaHeartBeatRequest {

    private String originator;
    private String timestamp;
}
