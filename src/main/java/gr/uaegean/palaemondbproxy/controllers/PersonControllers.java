package gr.uaegean.palaemondbproxy.controllers;

import gr.uaegean.palaemondbproxy.model.*;
import gr.uaegean.palaemondbproxy.model.TO.*;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import gr.uaegean.palaemondbproxy.repository.GeofenceRepository;
import gr.uaegean.palaemondbproxy.service.ElasticService;
import gr.uaegean.palaemondbproxy.service.KafkaService;
import gr.uaegean.palaemondbproxy.utils.CryptoUtils;
import gr.uaegean.palaemondbproxy.utils.PameasPersonFactory;
import gr.uaegean.palaemondbproxy.utils.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class PersonControllers {

    @Autowired
    KafkaService kafkaService;

    @Autowired
    CryptoUtils cryptoUtils;

    @Autowired
    ElasticService elasticService;

    @Autowired
    GeofenceRepository geofenceRepository;

    @PostMapping("/addPerson")
    public void addPerson(@RequestBody PersonTO person) {
        try {
            person.setName(cryptoUtils.encryptBase64(person.getName()));
            person.setSurname(cryptoUtils.encryptBase64(person.getSurname()));
            person.setIdentifier(cryptoUtils.encryptBase64(person.getIdentifier()));
            if (person.getConnectedPassengers() != null) {
                person.getConnectedPassengers().forEach(connectedPersonTO -> {
                    try {
                        connectedPersonTO.setName(cryptoUtils.encryptBase64(connectedPersonTO.getName()));
                        connectedPersonTO.setSurname(cryptoUtils.encryptBase64(connectedPersonTO.getSurname()));

                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException e) {
                        log.error("Error in encrypting person ");
                        log.error(e.getMessage());
                    }
                });
            }


        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.error("Error in encrypting person ");
            log.error(e.getMessage());
        }
        PameasPerson wrappedPerson = PameasPersonFactory.getFromPersonTO(person);
        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setDeviceInfoList(new ArrayList<>());
        networkInfo.setBraceletId(person.getBracelet());
        wrappedPerson.setNetworkInfo(networkInfo);

//        log.info(wrappedPerson.toString());
//        kafkaService.savePerson(wrappedPerson);
        elasticService.save(wrappedPerson);

    }


    @PostMapping("/addPerson2ES")
    public void addPerson2ES(@RequestBody PersonFullTO person) {
        try {
            person.setName(cryptoUtils.encryptBase64(person.getName()));
            person.setSurname(cryptoUtils.encryptBase64(person.getSurname()));
            person.setIdentifier(cryptoUtils.encryptBase64(person.getIdentifier()));
            if (person.getConnectedPassengers() != null) {
                person.getConnectedPassengers().forEach(connectedPersonTO -> {
                    try {
                        connectedPersonTO.setName(cryptoUtils.encryptBase64(connectedPersonTO.getName()));
                        connectedPersonTO.setSurname(cryptoUtils.encryptBase64(connectedPersonTO.getSurname()));

                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException e) {
                        log.error("Error in encrypting person ");
                        log.error(e.getMessage());
                    }
                });
            }


        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.error("Error in encrypting person ");
            log.error(e.getMessage());
        }
        PameasPerson wrappedPerson = PameasPersonFactory.getFromPersonFullTO(person);
//        log.info(wrappedPerson.toString());
//        kafkaService.savePerson(wrappedPerson);
        elasticService.save(wrappedPerson);

    }


    @PostMapping("/addPerson2ESBulk")
    public void addPerson2ESBulk(@RequestBody List<PersonFullTO> persons) {
        persons.stream().forEach(person -> {
            try {
                person.setName(cryptoUtils.encryptBase64(person.getName()));
                person.setSurname(cryptoUtils.encryptBase64(person.getSurname()));
                person.setIdentifier(cryptoUtils.encryptBase64(person.getIdentifier()));
                if (person.getConnectedPassengers() != null) {
                    person.getConnectedPassengers().forEach(connectedPersonTO -> {
                        try {
                            connectedPersonTO.setName(cryptoUtils.encryptBase64(connectedPersonTO.getName()));
                            connectedPersonTO.setSurname(cryptoUtils.encryptBase64(connectedPersonTO.getSurname()));

                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                                 IllegalBlockSizeException | BadPaddingException e) {
                            log.error("Error in encrypting person ");
                            log.error(e.getMessage());
                        }
                    });
                }
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                     IllegalBlockSizeException |
                     BadPaddingException e) {
                log.error("Error in encrypting person ");
                log.error(e.getMessage());
            }
            PameasPerson wrappedPerson = PameasPersonFactory.getFromPersonFullTO(person);
            elasticService.save(wrappedPerson);
        });
    }


    @GetMapping("/getPerson")
    public PameasPerson getPersonalInfo(@RequestParam String id) {
        Optional<PameasPerson> pameasPerson = Optional.empty();
        //            String encryptedId = this.cryptoUtils.encryptBase64(id);
//            log.info(encryptedId);
//            log.info(this.cryptoUtils.decryptBase64Message(encryptedId));
        pameasPerson = elasticService.getPersonByPersonalIdentifierDecrypted(id);
        if (pameasPerson.isPresent()) {
            Personalinfo personalinfo = pameasPerson.get().getPersonalInfo();
            List<TicketInfo> ticketInfoList = pameasPerson.get().getPersonalInfo().getTicketInfo();

            try {
                personalinfo.setPersonalId(cryptoUtils.decryptBase64Message(personalinfo.getPersonalId()));
                personalinfo.setName(cryptoUtils.decryptBase64Message(personalinfo.getName()));
                personalinfo.setSurname(cryptoUtils.decryptBase64Message(personalinfo.getSurname()));
                pameasPerson.get().setPersonalInfo(personalinfo);
                pameasPerson.get().getPersonalInfo().setTicketInfo(ticketInfoList.stream().map(ticketInfo -> {
                    try {
                        ticketInfo.setName(cryptoUtils.decryptBase64Message(ticketInfo.getName()));
                        ticketInfo.setSurname(cryptoUtils.decryptBase64Message(ticketInfo.getSurname()));
                        return ticketInfo;
                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException e) {
                        log.error("ERROR decrypting TicketInfo details");
                        log.error(e.getMessage());
                        return null;
                    }
                }).collect(Collectors.toList()));

            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                log.error("ERROR decrypting person details");
                log.error(e.getMessage());
            }
        }
        return pameasPerson.orElse(null);
    }


    @GetMapping("/getPersonReportById")
    public PersonReportTO getPersonReportById(@RequestParam String id) {
        Optional<PameasPerson> pameasPerson = elasticService.getPersonByPersonalIdentifierDecrypted(id);
        if (pameasPerson.isPresent()) {
            try {
                return Wrappers.pameasPerson2PersonReport(pameasPerson.get(), this.cryptoUtils);
            } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                     BadPaddingException | InvalidKeyException e) {
                log.error(e.getMessage());
                return null;
            }
        }
        return null;
    }


    @GetMapping("/findUserFromMumbleName")
    public PameasPerson findUserFromMumbleName(@RequestParam String mumbleName) {
        Optional<PameasPerson> pameasPerson = Optional.empty();
        pameasPerson = elasticService.getPersonByMumbleName(mumbleName);
        log.info("looking for person with {}", mumbleName);
        if (pameasPerson.isPresent()) {
            Personalinfo personalinfo = pameasPerson.get().getPersonalInfo();
            List<TicketInfo> ticketInfoList = pameasPerson.get().getPersonalInfo().getTicketInfo();
            try {
                personalinfo.setPersonalId(cryptoUtils.decryptBase64Message(personalinfo.getPersonalId()));
                personalinfo.setName(cryptoUtils.decryptBase64Message(personalinfo.getName()));
                personalinfo.setSurname(cryptoUtils.decryptBase64Message(personalinfo.getSurname()));
                pameasPerson.get().setPersonalInfo(personalinfo);
                pameasPerson.get().getPersonalInfo().setTicketInfo(ticketInfoList.stream().map(ticketInfo -> {
                    try {
                        ticketInfo.setName(cryptoUtils.decryptBase64Message(ticketInfo.getName()));
                        ticketInfo.setSurname(cryptoUtils.decryptBase64Message(ticketInfo.getSurname()));
                        return ticketInfo;
                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException e) {
                        log.error("ERROR decrypting TicketInfo details");
                        log.error(e.getMessage());
                        return null;
                    }
                }).collect(Collectors.toList()));
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                log.error("ERROR decrypting person details");
                log.error(e.getMessage());
            }
        } else {
            log.info("no user with {} found", mumbleName);
        }
        return pameasPerson.orElse(null);
    }


    @GetMapping("/getPersonByHashedMac")
    public PameasPerson getPersonByHashedMac(@RequestParam String hashedMac) {
        Optional<PameasPerson> pameasPerson = Optional.empty();
        pameasPerson = elasticService.getPersonByHashedMacAddress(hashedMac);
        if (pameasPerson.isPresent()) {
            Personalinfo personalinfo = pameasPerson.get().getPersonalInfo();
            List<TicketInfo> ticketInfoList = pameasPerson.get().getPersonalInfo().getTicketInfo();
            try {
                personalinfo.setPersonalId(cryptoUtils.decryptBase64Message(personalinfo.getPersonalId()));
                personalinfo.setName(cryptoUtils.decryptBase64Message(personalinfo.getName()));
                personalinfo.setSurname(cryptoUtils.decryptBase64Message(personalinfo.getSurname()));
                pameasPerson.get().setPersonalInfo(personalinfo);
                pameasPerson.get().getPersonalInfo().setTicketInfo(ticketInfoList.stream().map(ticketInfo -> {
                    try {
                        ticketInfo.setName(cryptoUtils.decryptBase64Message(ticketInfo.getName()));
                        ticketInfo.setSurname(cryptoUtils.decryptBase64Message(ticketInfo.getSurname()));
                        return ticketInfo;
                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException e) {
                        log.error("ERROR decrypting TicketInfo details");
                        log.error(e.getMessage());
                        return null;
                    }
                }).collect(Collectors.toList()));
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                log.error("ERROR decrypting person details");
                log.error(e.getMessage());
            }
        }
        return pameasPerson.orElse(null);
    }


    @GetMapping("/getAll")
    public @ResponseBody
    List<PameasPerson> getPersonalInfo() {
        return elasticService.getAllPersonsDecrypted();
    }

    @GetMapping("/getPassengers")
    public @ResponseBody
    List<PameasPerson> getAllPassengerInfo() {
//        return elasticService.getAllPersonsDecrypted().stream()
//                .filter(pameasPerson -> pameasPerson.getPersonalInfo().getRole().equals("passenger")).collect(Collectors.toList());
        return elasticService.getAllPassengersDecrypted();
    }


    @GetMapping("/getCrewMembersList")
    public @ResponseBody
    List<CrewMemberListEntryTO> getAllCrewMembersList() {
        List<PameasPerson> allCrew = elasticService.getAllCrewMembersDecrypted();
        return allCrew.stream().map(person -> {
            CrewMemberListEntryTO res = new CrewMemberListEntryTO();
            res.setName(person.getPersonalInfo().getName());
            res.setSurname(person.getPersonalInfo().getSurname());
            res.setTeam(person.getPersonalInfo().getEmergencyDuty());
            if (person.getLocationInfo() != null && person.getLocationInfo().getGeofenceHistory() != null
                    && person.getLocationInfo().getGeofenceHistory().size() > 0) {
                res.setLocation(person.getLocationInfo().getGeofenceHistory().get(person.getLocationInfo().getGeofenceHistory().size() - 1).getGfName());
            }
            return res;
        }).collect(Collectors.toList());


    }


    @GetMapping("/getPassengersPerAssignedGeofence")
    public @ResponseBody
    PassengerMusteringStatusTO getPassengersPerAssignedGeofence(@RequestParam String geofenceName) {
        PassengerMusteringStatusTO musteringStatusTO = new PassengerMusteringStatusTO();

        List<PameasPerson> allPassengers = elasticService.getAllPersonsDecrypted().stream()
                .filter(pameasPerson -> {

                    return pameasPerson.getPersonalInfo().getAssignedMusteringStation() != null &&
                            (pameasPerson.getPersonalInfo().getRole().equals("passenger")
                                    && pameasPerson.getPersonalInfo().getAssignedMusteringStation().equals(geofenceName));
                }).collect(Collectors.toList());

        List<PameasPerson> passengersAtMS = allPassengers.stream().filter(pameasPerson -> {
            int locationsSize = pameasPerson.getLocationInfo().getLocationHistory().size();
            return pameasPerson.getLocationInfo().getGeofenceHistory().get(locationsSize - 1).getGfName().equals(geofenceName);
        }).collect(Collectors.toList());

        musteringStatusTO.setPassengersAssigned(allPassengers.size());
        musteringStatusTO.setPassengersMustered(passengersAtMS.size());
        musteringStatusTO.setPassengersMissing(allPassengers.size() - passengersAtMS.size());

//        List<Geofence> geofences = new ArrayList<>();
//        geofenceRepository.findAll().forEach(geofences::add);
//        Optional<Geofence> ms =  geofences.stream().filter(geofence -> geofence.getGfName().equals(geofenceName)).findFirst().get().setGfName();

        Optional<Geofence> geofence = elasticService.getGeofenceByName(geofenceName);
        geofence.ifPresent(value -> musteringStatusTO.setOpen(value.getStatus().equalsName("OPEN")));


        return musteringStatusTO;
    }


    @GetMapping("/getPassengerVisualizationData")
    public @ResponseBody
    List<PassengerVisualizationTO> getPassengerVisualizationData() {

        return elasticService.getAllPassengersDecrypted().stream().map(pameasPerson -> {
            PassengerVisualizationTO visualizationTO = new PassengerVisualizationTO();
            int locationSize = pameasPerson.getLocationInfo().getLocationHistory().size();
            visualizationTO.setXLoc(pameasPerson.getLocationInfo().getLocationHistory().get(locationSize - 1).getXLocation());
            visualizationTO.setYLoc(pameasPerson.getLocationInfo().getLocationHistory().get(locationSize - 1).getYLocation());
            visualizationTO.setAssignedMS(pameasPerson.getPersonalInfo().getAssignedMusteringStation());
            ArrayList<String> mobilityIssues = new ArrayList<>();
            if (!StringUtils.isEmpty(pameasPerson.getPersonalInfo().getMedicalCondition())) {
                mobilityIssues.add(pameasPerson.getPersonalInfo().getMedicalCondition());
            }
            if (!StringUtils.isEmpty(pameasPerson.getPersonalInfo().getPrengencyData())) {
                mobilityIssues.add(pameasPerson.getPersonalInfo().getPrengencyData());
            }
            if (!StringUtils.isEmpty(pameasPerson.getPersonalInfo().getMobilityIssues())) {
                mobilityIssues.add(pameasPerson.getPersonalInfo().getMobilityIssues());
            }

            visualizationTO.setMobility(mobilityIssues);
            visualizationTO.setGeofence(pameasPerson.getLocationInfo().getGeofenceHistory().get(locationSize - 1).getGfName());
            visualizationTO.setType(pameasPerson.getPersonalInfo().isCrew() ? "Crew" : "Passenger");
            visualizationTO.setInAssignedMS(
                    pameasPerson.getPersonalInfo().getAssignedMusteringStation()
                            .equals(pameasPerson.getLocationInfo().getGeofenceHistory().get(locationSize - 1).getGfName()));
            visualizationTO.setDeck(getDeckFromGeofence(visualizationTO.getGeofence()));
            return visualizationTO;
        }).collect(Collectors.toList());


    }


    @GetMapping("/getPersonVisualizationData")
    public @ResponseBody
    List<PassengerVisualizationTO> getPersonVisualizationData() {

        return elasticService.getAllPersonsDecrypted().stream().map(pameasPerson -> {
            PassengerVisualizationTO visualizationTO = new PassengerVisualizationTO();
            if (pameasPerson.getLocationInfo() == null || pameasPerson.getLocationInfo().getLocationHistory() == null
                    || pameasPerson.getLocationInfo().getGeofenceHistory() == null
                    || pameasPerson.getLocationInfo().getLocationHistory().size() == 0)
                return visualizationTO;

            int locationSize = pameasPerson.getLocationInfo().getLocationHistory().size();
            visualizationTO.setXLoc(pameasPerson.getLocationInfo().getLocationHistory().get(locationSize - 1).getXLocation());
            visualizationTO.setYLoc(pameasPerson.getLocationInfo().getLocationHistory().get(locationSize - 1).getYLocation());
            visualizationTO.setAssignedMS(pameasPerson.getPersonalInfo().getAssignedMusteringStation());
            ArrayList<String> mobilityIssues = new ArrayList<>();
            if (!StringUtils.isEmpty(pameasPerson.getPersonalInfo().getMedicalCondition())) {
                mobilityIssues.add(pameasPerson.getPersonalInfo().getMedicalCondition());
            }
            if (!StringUtils.isEmpty(pameasPerson.getPersonalInfo().getPrengencyData())) {
                mobilityIssues.add(pameasPerson.getPersonalInfo().getPrengencyData());
            }
            if (!StringUtils.isEmpty(pameasPerson.getPersonalInfo().getMobilityIssues())) {
                mobilityIssues.add(pameasPerson.getPersonalInfo().getMobilityIssues());
            }

            visualizationTO.setMobility(mobilityIssues);
            visualizationTO.setGeofence(pameasPerson.getLocationInfo().getGeofenceHistory().get(locationSize - 1).getGfName());
            visualizationTO.setType(pameasPerson.getPersonalInfo().isCrew() ? "Crew" : "Passenger");
            if (pameasPerson.getPersonalInfo().getAssignedMusteringStation() != null) {
                visualizationTO.setInAssignedMS(
                        pameasPerson.getPersonalInfo().getAssignedMusteringStation()
                                .equals(pameasPerson.getLocationInfo().getGeofenceHistory().get(locationSize - 1).getGfName()));
            }
            visualizationTO.setDeck(getDeckFromGeofence(visualizationTO.getGeofence()));
            return visualizationTO;
        }).collect(Collectors.toList());


    }


    @GetMapping("/getPersonPosition")
    public @ResponseBody
    LatestLocationTO getPersonPosition(@RequestParam String id) {
        LatestLocationTO result = new LatestLocationTO();
        Optional<PameasPerson> pameasPerson = Optional.empty();
        pameasPerson = elasticService.getPersonByPersonalIdentifierDecrypted(id);

        if (pameasPerson.isPresent()) {
            UserLocationUnit latestLocation =
                    pameasPerson.get().getLocationInfo().getLocationHistory().get(pameasPerson.get().getLocationInfo().getLocationHistory().size() - 1);
            UserGeofenceUnit latestGeofenceUnit =
                    pameasPerson.get().getLocationInfo().getGeofenceHistory().get(pameasPerson.get().getLocationInfo().getGeofenceHistory().size() - 1);
            Optional<Geofence> latestGeofence = geofenceRepository.findById(latestGeofenceUnit.getGfId());
            if (latestGeofence.isPresent()) {
                result.setGeofence(latestGeofence.get());
                result.setLocation(latestLocation);
            }
        }
        return result;
    }


    @GetMapping("/getPassengerCommunicationDetails")
    public @ResponseBody
    List<PassengerCommunicationDetailsTO> getPassengerCommunicationDetails() {

        return elasticService.getAllPersonsDecrypted().stream()
//                .filter(pameasPerson -> pameasPerson.getPersonalInfo().getRole().equals("passenger"))
                .map(pameasPerson -> {
                    PassengerCommunicationDetailsTO detailsTO = new PassengerCommunicationDetailsTO();
                    detailsTO.setMacAddress(pameasPerson.getNetworkInfo().getDeviceInfoList().get(0).getMacAddress());
                    String[] preferredLanguages = new String[pameasPerson.getPersonalInfo().getPreferredLanguage().size()];
                    detailsTO.setPreferredLanguage(pameasPerson.getPersonalInfo().getPreferredLanguage().toArray(preferredLanguages));
                    return detailsTO;
                }).collect(Collectors.toList());
    }


    @GetMapping("/getPassengerMusteringDetails")
    public @ResponseBody
    List<PassengerMusteringDetailsTO> getPassengerMusteringDetails() {

        return elasticService.getAllPassengersDecrypted().stream()
//                .filter(pameasPerson -> pameasPerson.getPersonalInfo().getRole().equals("passenger"))
                .map(pameasPerson -> {
                    PassengerMusteringDetailsTO detailsTO = new PassengerMusteringDetailsTO();
                    detailsTO.setGeneralInfo(PameasPersonFactory.transformPameasPerson2PersonTO(pameasPerson));
                    detailsTO.setCommunicationDetails(transformPameasPerson2PassengerCommunicationDetailsTO(pameasPerson));
                    detailsTO.setLastKnowLocation(transformPameasPerson2LatestLocation(pameasPerson));
                    return detailsTO;
                })
                .collect(Collectors.toList());
    }


    @PostMapping("/updatePassengerPath")
    public @ResponseBody
    String updatePassengerPath(@RequestBody UpdatePersonStatusTO person) {
        Optional<PameasPerson> existingPerson = elasticService.getPersonByHashedMacAddress(person.getHashedMacAddress());
        existingPerson.ifPresent(pameasPerson -> pameasPerson.getPersonalInfo().setAssignedPath(person.getPath()));
        existingPerson.ifPresent(pameasPerson -> elasticService.save(pameasPerson));
        return "OK";
    }


    @PostMapping("/updatePassengerMS")
    public @ResponseBody
    String updatePassengerMS(@RequestBody UpdatePersonStatusTO person) {
        Optional<PameasPerson> existingPerson = elasticService.getPersonByHashedMacAddress(person.getHashedMacAddress());
        existingPerson.ifPresent(pameasPerson -> pameasPerson.getPersonalInfo().setAssignedMusteringStation(person.getMusteringStation()));
        existingPerson.ifPresent(pameasPerson -> elasticService.save(pameasPerson));
        return "OK";
    }

    @PostMapping("/updatePerson")
    public @ResponseBody
    String updatePassengerMS(@RequestBody PameasPerson person) {
        Optional<PameasPerson> existingPerson =
                elasticService.getPersonByHashedMacAddress(person.getNetworkInfo().getDeviceInfoList().get(0).getHashedMacAddress());
        existingPerson.ifPresent(pameasPerson -> {
                    try {
                        String decryptedPersonaId = this.cryptoUtils.decryptBase64Message(person.getPersonalInfo().getPersonalId());
                        elasticService.updatePerson(decryptedPersonaId, person);
                    } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |
                             InvalidKeyException | NoSuchAlgorithmException e) {
                        log.error(e.getMessage());
                        elasticService.updatePerson(person.getPersonalInfo().getPersonalId(), person);
                    }
                }
        );
        return "OK";
    }


    @PostMapping("/updatePassengerMSBulk")
    public @ResponseBody
    String updatePassengerMSBulk(@RequestBody UpdatePersonStatusTO[] persons) {
        Arrays.stream(persons).forEach(person -> {
            Optional<PameasPerson> existingPerson = elasticService.getPersonByHashedMacAddress(person.getHashedMacAddress());
            existingPerson.ifPresent(pameasPerson -> pameasPerson.getPersonalInfo().setAssignedMusteringStation(person.getMusteringStation()));
            existingPerson.ifPresent(pameasPerson -> elasticService.save(pameasPerson));
        });

        return "OK";
    }


    @PostMapping("/updateCrewInPosition")
    public @ResponseBody
    String updateCrewInPosition(@RequestBody UpdatePersonStatusTO person) {
        Optional<PameasPerson> existingPerson = elasticService.getPersonByHashedMacAddress(person.getHashedMacAddress());
        existingPerson.ifPresent(pameasPerson -> pameasPerson.
                getPersonalInfo().setInPosition(person.isInPosition()));
        existingPerson.ifPresent(pameasPerson -> elasticService.save(pameasPerson));
        return "OK";
    }


    @PostMapping("/updateSaturation")
    public @ResponseBody
    String monitorBraceletSaturation(BraceletDataTO braceletDataTO) {

        Optional<PameasPerson> person = elasticService.getPersonByBraceletId(braceletDataTO.getId());
        try {
            if (person.isPresent()) {
                person.get().getPersonalInfo().setOxygenSaturation(braceletDataTO.getSp02());
                String decryptedPersonaId = this.cryptoUtils.decryptBase64Message(person.get().getPersonalInfo().getPersonalId());
                elasticService.updatePerson("", person.get());
                return "OK";
            } else {
                log.error("no person found with bracelet {}", braceletDataTO.getId());
            }
        } catch (InvalidKeyException | BadPaddingException | NoSuchAlgorithmException |
                 IllegalBlockSizeException | NoSuchPaddingException e) {
            log.error(e.getMessage());
        }
        return "ERROR";
    }


    @GetMapping("/getPassengersPerGeofence")
    public List<PassengersListPerGeoTO> getPassengersPerGeofence() {
        List<Geofence> geofences = this.elasticService.getGeofences();
        List<PameasPerson> passengers = this.elasticService.getAllPassengersDecrypted();
        AtomicReference<String> label = new AtomicReference<>("");

        return geofences.stream().map(geofence -> {
            String geoName = geofence.getGfName();
            switch (geofence.getGfName()) {
                case "9CG0":
                    label.set("Muster Station D");
                    break;
                case "9BG1":
                    label.set("VIP lounge");
                    break;
                case "9BG2":
                    label.set("Restaurant");
                    break;
                case "9BG1+":
                    label.set("Staircase");
                    break;
                case "9BG4":
                    label.set("Cabin corridor");
                    break;
                case "9BG3":
                    label.set("corridor");
                    break;
                default:
                    label.set(geofence.getGfName());
                    break;
            }

            List<PassengerPIMMTO> pList = passengers.stream().filter(person -> {
                        if (person.getLocationInfo().getGeofenceHistory() != null) {
                            int sizeOfLocations = person.getLocationInfo().getGeofenceHistory().size();
                            if (sizeOfLocations > 0) {
                                return geoName.equals(person.getLocationInfo().getGeofenceHistory().get(sizeOfLocations - 1).getGfName());
                            }
                        }
                        return false;
                    }).map(Wrappers::pameasPerson2PassengerPIMMTO).map(passengerPIMMTO -> {

                        return passengerPIMMTO;
                    })
                    .collect(Collectors.toList());
            PassengersListPerGeoTO passengerPIMMTO = new PassengersListPerGeoTO();
            passengerPIMMTO.setGeofence(geoName);
            passengerPIMMTO.setPassengers(pList);
            passengerPIMMTO.setLabel(label.get());

            return passengerPIMMTO;
        }).collect(Collectors.toList());
    }


    public LatestLocationTO transformPameasPerson2LatestLocation(PameasPerson pameasPerson) {
        LatestLocationTO result = new LatestLocationTO();

        UserLocationUnit latestLocation =
                pameasPerson.getLocationInfo().getLocationHistory().get(pameasPerson.getLocationInfo().getLocationHistory().size() - 1);
        UserGeofenceUnit latestGeofenceUnit =
                pameasPerson.getLocationInfo().getGeofenceHistory().get(pameasPerson.getLocationInfo().getGeofenceHistory().size() - 1);
        Optional<Geofence> latestGeofence = geofenceRepository.findById(latestGeofenceUnit.getGfId());
        if (latestGeofence.isPresent()) {
            result.setGeofence(latestGeofence.get());
            result.setLocation(latestLocation);
        }
        return result;
    }


    public PassengerCommunicationDetailsTO transformPameasPerson2PassengerCommunicationDetailsTO(PameasPerson pameasPerson) {
        String[] preferredLanguages = new String[pameasPerson.getPersonalInfo().getPreferredLanguage().size()];
        PassengerCommunicationDetailsTO communicationDetailsTO = new PassengerCommunicationDetailsTO();
        communicationDetailsTO.setMacAddress(pameasPerson.getNetworkInfo().getDeviceInfoList().get(0).getMacAddress());
        communicationDetailsTO.setPreferredLanguage(pameasPerson.getPersonalInfo().getPreferredLanguage().toArray(preferredLanguages));
        return communicationDetailsTO;
    }


    public String getDeckFromGeofence(String geofence) {
        if (geofence.startsWith("7") || geofence.startsWith("S7")) {
            return "Deck7";
        }
        if (geofence.startsWith("8") || geofence.startsWith("S8")) {
            return "Deck8";
        }

        if (geofence.startsWith("9") || geofence.startsWith("S")) {
            return "Deck9";
        }
        return "ERROR";
    }

}
