package gr.uaegean.palaemondbproxy;

import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.model.location.UserGeofenceUnit;
import gr.uaegean.palaemondbproxy.model.location.UserLocationUnit;
import gr.uaegean.palaemondbproxy.service.SpeedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class DistanceSpeedTests {

    @Autowired
    private SpeedService speedService;

    @Test
    public void testSpeedCalc() {
        LocationTO newLocation = new LocationTO();
        UserLocationUnit newUserLocUnit = new UserLocationUnit();
        newUserLocUnit.setBuildingId("b1");
        newUserLocUnit.setGeofenceId("7BG6");
        newUserLocUnit.setGeofenceNames(List.of("7BG6"));
        newUserLocUnit.setTimestamp("2022-07-04 11:50:45");
        newUserLocUnit.setXLocation("132.70166209036013");
        newUserLocUnit.setYLocation("9.170131874177427");
        newLocation.setLocation(newUserLocUnit);

        UserGeofenceUnit newUserGeofenceUnit = new UserGeofenceUnit();
        newUserGeofenceUnit.setDeck("7");
        newLocation.setGeofence(newUserGeofenceUnit);


        newLocation.setHashedMacAddress("395fc9d45956c0b76ecbd13522294375389df642d20d0a2ade5fb44a708ee464");

        speedService.updatePersonSpeed(newLocation);



    }

}
