package gr.uaegean.palaemondbproxy.model.TO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MinLocationUnitTO {
    @JsonProperty("xLocation")
    private String xLocation;
    @JsonProperty("yLocation")
    private String yLocation;
    private String timestamp;
}
