package gr.uaegean.palaemondbproxy.model.TO;

import gr.uaegean.palaemondbproxy.model.DutySchedule;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignScheduleTO {

    List<DutySchedule> dutySchedule;
    String personId;
}
