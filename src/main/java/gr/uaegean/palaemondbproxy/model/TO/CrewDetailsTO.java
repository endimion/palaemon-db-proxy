package gr.uaegean.palaemondbproxy.model.TO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrewDetailsTO {

    public List<PersonTO> onDuty ;
    public List<PersonTO> offDuty ;
}
