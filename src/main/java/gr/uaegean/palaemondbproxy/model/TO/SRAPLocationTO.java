package gr.uaegean.palaemondbproxy.model.TO;

import gr.uaegean.palaemondbproxy.model.Personalinfo;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SRAPLocationTO implements Serializable {

    private String id;
    private Personalinfo personalinfo;

}
