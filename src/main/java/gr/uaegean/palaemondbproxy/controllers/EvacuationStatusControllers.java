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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class EvacuationStatusControllers {


    @Autowired
    ElasticService elasticService;


    @PostMapping("/setEvacuationStatus")
    public void addPerson(@RequestBody EvacuationStatus evacuationStatus) {
        elasticService.saveEvacuationStatus(evacuationStatus);
    }

    @GetMapping("/getEvacuationStatus")
    public EvacuationStatus getPersonalInfo() {
        EvacuationStatus resut = new EvacuationStatus();
        elasticService.getEvacuationStatus().ifPresent(evacuationStatus -> {
            resut.setStatus(evacuationStatus.getStatus());
            resut.setId(evacuationStatus.getId());
        });

        return resut;
    }


}
