package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.DutySchedule;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import gr.uaegean.palaemondbproxy.model.Personalinfo;
import gr.uaegean.palaemondbproxy.model.TO.*;
import gr.uaegean.palaemondbproxy.repository.PameasPersonRepository;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.utils.PameasPersonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CrewMemberControllers {

    @Autowired
    PameasPersonRepository personRepository;

    @Autowired
    ElasticService elasticService;


    @GetMapping("/getCrew")
    public @ResponseBody
    List<PameasPerson> getAllCrewInfo() {
        return elasticService.getAllPersonsDecrypted().stream()
                .filter(pameasPerson -> !(pameasPerson.getPersonalInfo().getRole() == null ||
                        pameasPerson.getPersonalInfo().getRole().equals("passenger"))).collect(Collectors.toList());
    }

    @GetMapping("/getCrewAndDuties")
    public CrewDetailsTO getAllCrewMembersDetails() {
        List<PameasPerson> crewMembers = personRepository.findCrewMembers();

        CrewDetailsTO result = new CrewDetailsTO();

        List<PersonTO> onDuty = new ArrayList<>();
        List<PersonTO> offDuty = new ArrayList<>();
        crewMembers.forEach(pameasPerson -> {
            pameasPerson.getPersonalInfo().getDutyScheduleList().stream().forEach(dutySchedule -> {
                if (dutySchedule.getDutyStartDateTime().isBefore(LocalDateTime.now())
                        && dutySchedule.getDutyEndDateTime().isAfter(LocalDateTime.now())) {
                    onDuty.add(PameasPersonFactory.transformPameasPerson2PersonTO(pameasPerson));
                } else {
                    offDuty.add(PameasPersonFactory.transformPameasPerson2PersonTO(pameasPerson));
                }
            });
        });

        result.setOnDuty(onDuty);
        result.setOffDuty(offDuty);

        return result;
    }


    @PostMapping("/assignDuties")
    public @ResponseBody
    String assignCrewMemberSchedule(@RequestBody AssignScheduleTO newSchedule) {
        Optional<PameasPerson> crewMembers = elasticService.getPersonByPersonalIdentifierDecrypted(newSchedule.getPersonId());
        if (crewMembers.isPresent()) {
            crewMembers.get().getPersonalInfo().setDutyScheduleList(newSchedule.getDutySchedule());
            personRepository.save(crewMembers.get());
        } else {
            return "ERROR";
        }

        return "OK";

    }

    @PostMapping("/updateCrewMemberStatus")
    public @ResponseBody
    String updateCrewMemberStatus(@RequestBody UpdatePersonStatusTO newStatus) {
        Optional<PameasPerson> crewMembers = elasticService.getPersonByPersonalIdentifierDecrypted(newStatus.getId());
        if (crewMembers.isPresent()) {
            crewMembers.get().getPersonalInfo().setInPosition(newStatus.isInPosition());
            crewMembers.get().getPersonalInfo().setAssignmentStatus(newStatus.getAssignmentStatus());
            crewMembers.get().getPersonalInfo().setAssignedMusteringStation(newStatus.getMusteringStation());
            log.info("will save {}", crewMembers.get());
            personRepository.save(crewMembers.get());
        } else {
            return "ERROR";
        }
        return "OK";

    }


    @GetMapping("/getCrewInPositionStatus")
    public CrewPositionStatusTO getCrewInPositionStatus() {
        List<PameasPerson> crewMembers = personRepository.findCrewMembers();
        List<PersonTO> inPosition = new ArrayList<>();
        List<PersonTO> notInPosition = new ArrayList<>();
        crewMembers.forEach(pameasPerson -> {
            if (pameasPerson.getPersonalInfo().isInPosition()) {
                inPosition.add(PameasPersonFactory.transformPameasPerson2PersonTO(pameasPerson));
            } else {
                notInPosition.add(PameasPersonFactory.transformPameasPerson2PersonTO(pameasPerson));
            }
        });
        CrewPositionStatusTO result = new CrewPositionStatusTO();
        result.setInPosition(inPosition);
        result.setNotInPosition(notInPosition);

        return result;
    }


}
