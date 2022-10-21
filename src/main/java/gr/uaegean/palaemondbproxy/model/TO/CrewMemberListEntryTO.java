package gr.uaegean.palaemondbproxy.model.TO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrewMemberListEntryTO {

    private String name;
    private String surname;
    private String team;
    private String location;

}
