package gr.uaegean.palaemondbproxy.utils;

import gr.uaegean.palaemondbproxy.model.*;
import gr.uaegean.palaemondbproxy.model.TO.ConnectedPersonTO;
import gr.uaegean.palaemondbproxy.model.TO.PersonTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PameasPersonFactory {


    public static PameasPerson getFromPersonTO(PersonTO receivedPerson) {
        PameasPerson p = new PameasPerson();
        Personalinfo pii = new Personalinfo();
        pii.setPersonalId(receivedPerson.getIdentifier());
        pii.setSurname(receivedPerson.getSurname());
        pii.setName(receivedPerson.getName());
        pii.setGender(receivedPerson.getGender());

        pii.setTicketNumber(receivedPerson.getTicketNumber());
        List<TicketInfo> receivedLinkedTickets =
                receivedPerson.getConnectedPassengers() != null ?
                        receivedPerson.getConnectedPassengers().stream().map(pass -> {
                            TicketInfo ti = new TicketInfo();
                            ti.setTicketNumber(pass.getTicketNumber());
                            ti.setGender(pass.getGender());
                            ti.setName(pass.getName());
                            ti.setSurname(pass.getSurname());
                            ti.setDateOfBirth(pass.getAge());
                            return ti;
                        }).collect(Collectors.toList()) : new ArrayList<>();

        pii.setTicketInfo(receivedLinkedTickets);
        pii.setDateOfBirth(receivedPerson.getAge());
        pii.setEmbarkationPort(receivedPerson.getEmbarkationPort());
        pii.setDisembarkationPort(receivedPerson.getDisembarkationPort());
        pii.setEmail(receivedPerson.getEmail());
        pii.setCountryOfResidence(receivedPerson.getCountryOfResidence());
        pii.setPreferredLanguage(Arrays.asList(receivedPerson.getPreferredLanguage()));
        pii.setMedicalCondition(receivedPerson.getMedicalCondition());
        pii.setMobilityIssues(receivedPerson.getMobilityIssues());
        pii.setPrengencyData(receivedPerson.getPrengencyData());
        pii.setEmergencyContact(receivedPerson.getEmergencyContact());

        pii.setCrew(receivedPerson.isCrew());
        pii.setRole(receivedPerson.getRole());
        pii.setEmergencyDuty(receivedPerson.getEmergencyDuty());
        pii.setDutyScheduleList(receivedPerson.getDutySchedule());
        pii.setAssignedMusteringStation(receivedPerson.getAssignedMusteringStation());
        p.setPersonalInfo(pii);

        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setDeviceInfoList(new ArrayList<>());
        p.setNetworkInfo(networkInfo);
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLocationHistory(new ArrayList<>());
        locationInfo.setGeofenceHistory(new ArrayList<>());
        p.setLocationInfo(locationInfo);

        return p;
    }


    public static PersonTO transformPameasPerson2PersonTO(PameasPerson receivedPerson) {
        PersonTO p = new PersonTO();
        p.setIdentifier(receivedPerson.getPersonalInfo().getPersonalId());
        p.setSurname(receivedPerson.getPersonalInfo().getSurname());
        p.setName(receivedPerson.getPersonalInfo().getName());
        p.setGender(receivedPerson.getPersonalInfo().getGender());
        p.setTicketNumber(receivedPerson.getPersonalInfo().getTicketNumber());
        List<ConnectedPersonTO> connectedPersonTOList = new ArrayList<>();
        if (receivedPerson.getPersonalInfo().getTicketInfo() != null)
            receivedPerson.getPersonalInfo().getTicketInfo().forEach(ticketInfo -> {
                connectedPersonTOList.add(ticketInfo2ConnectedPersonTO(ticketInfo));
            });
        p.setConnectedPassengers(connectedPersonTOList);

//        List<TicketInfo> receivedLinkedTickets =
//                receivedPerson.getPersonalInfo().getTicketInfo() != null?
//                        receivedPerson.getPersonalInfo().getTicketInfo().stream().map(pass -> {
//                            TicketInfo ti = new TicketInfo();
//                            ti.setTicketNumber(pass.getTicketNumber());
//                            ti.setGender(pass.getGender());
//                            ti.setName(pass.getName());
//                            ti.setSurname(pass.getSurname());
//                            ti.setDateOfBirth(pass.getDateOfBirth());
//                            return ti;
//                        }).collect(Collectors.toList()): new ArrayList<>();

        p.setAge(receivedPerson.getPersonalInfo().getDateOfBirth());
        p.setEmbarkationPort(receivedPerson.getPersonalInfo().getEmbarkationPort());
        p.setDisembarkationPort(receivedPerson.getPersonalInfo().getDisembarkationPort());
        p.setEmail(receivedPerson.getPersonalInfo().getEmail());
        p.setCountryOfResidence(receivedPerson.getPersonalInfo().getCountryOfResidence());
        if(receivedPerson.getPersonalInfo().getPreferredLanguage() != null){
            String[] preferedLangatues = new String[receivedPerson.getPersonalInfo().getPreferredLanguage().size()];
            p.setPreferredLanguage(receivedPerson.getPersonalInfo().getPreferredLanguage().toArray(preferedLangatues));
        }else{
            p.setPreferredLanguage(new String[0]);
        }

        p.setMedicalCondition(receivedPerson.getPersonalInfo().getMedicalCondition());
        p.setMobilityIssues(receivedPerson.getPersonalInfo().getMobilityIssues());
        p.setPrengencyData(receivedPerson.getPersonalInfo().getPrengencyData());
        p.setEmergencyContact(receivedPerson.getPersonalInfo().getEmergencyContact());

        p.setCrew(receivedPerson.getPersonalInfo().isCrew());
        p.setRole(receivedPerson.getPersonalInfo().getRole());
        p.setEmergencyDuty(receivedPerson.getPersonalInfo().getEmergencyDuty());
        p.setDutySchedule(receivedPerson.getPersonalInfo().getDutyScheduleList());

        p.setInPosition(receivedPerson.getPersonalInfo().isInPosition());
        p.setAssignedMusteringStation(receivedPerson.getPersonalInfo().getAssignedMusteringStation());
        p.setAssignmentStatus(receivedPerson.getPersonalInfo().getAssignmentStatus());

        return p;
    }


    public static ConnectedPersonTO ticketInfo2ConnectedPersonTO(TicketInfo ticket) {
        ConnectedPersonTO connectedPersonTO = new ConnectedPersonTO();
        connectedPersonTO.setSurname(ticket.getSurname());
        connectedPersonTO.setTicketNumber(ticket.getTicketNumber());
        connectedPersonTO.setGender(ticket.getGender());
        connectedPersonTO.setName(ticket.getName());
        connectedPersonTO.setAge(ticket.getDateOfBirth());
        return connectedPersonTO;
    }

}
