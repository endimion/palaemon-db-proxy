package gr.uaegean.palaemondbproxy.model.TO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrewPositionStatusTO {
    public List<PersonTO> inPosition;
    public List<PersonTO> notInPosition;
}
