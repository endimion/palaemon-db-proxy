package gr.uaegean.palaemondbproxy.service;

import gr.uaegean.palaemondbproxy.model.TO.LocationTO;

public interface SpeedService {

    //x,y in meters
    public double calculateDistance(double x1, double x2, double y1, double y2);

    //meters per minute
    public double calculateSpeed(double distance, long t1, long t2);


    public double updatePersonSpeed(LocationTO newLocation);
}
