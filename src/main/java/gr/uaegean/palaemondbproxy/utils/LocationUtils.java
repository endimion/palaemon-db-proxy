package gr.uaegean.palaemondbproxy.utils;

import gr.uaegean.palaemondbproxy.model.TO.LocationTO;
import gr.uaegean.palaemondbproxy.model.TO.MinGeofenceUnitTO;
import gr.uaegean.palaemondbproxy.model.TO.MinLocationTO;
import gr.uaegean.palaemondbproxy.model.TO.MinLocationUnitTO;

public class LocationUtils {

    public static MinLocationTO reduceLocation(LocationTO location) {
        MinLocationTO minLocationTO = new MinLocationTO();
        minLocationTO.setHashedMacAddress(location.getHashedMacAddress());
        MinLocationUnitTO minLocationUnitTO = new MinLocationUnitTO();
        minLocationUnitTO.setXLocation(location.getLocation().getXLocation());
        minLocationUnitTO.setYLocation(location.getLocation().getYLocation());
        minLocationUnitTO.setTimestamp(location.getLocation().getTimestamp());
        minLocationTO.setLocation(minLocationUnitTO);

        MinGeofenceUnitTO geofenceUnitTO = new MinGeofenceUnitTO();
        geofenceUnitTO.setTimestamp(location.getGeofence().getTimestamp());
        geofenceUnitTO.setGfId(location.getGeofence().getGfId());
        geofenceUnitTO.setGfName(location.getGeofence().getGfName());
        geofenceUnitTO.setDeck(location.getGeofence().getDeck());

        minLocationTO.setGeofence(geofenceUnitTO);

        return minLocationTO;
    }
}
