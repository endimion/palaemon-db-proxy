package gr.uaegean.palaemondbproxy.model.TO;

import gr.uaegean.palaemondbproxy.model.Personalinfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonStatusTO {

    private Personalinfo.AssignmentStatus assignmentStatus;
    private boolean inPosition;
    private String musteringStation;
    private String id;
    private String hashedMacAddress;
    private String path;

}
