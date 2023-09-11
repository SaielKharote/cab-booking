package com.scaler.cabbooking.services;

import com.scaler.cabbooking.exceptions.DriverException;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.requests.DriverSignupRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverService {
    public Driver registerDriver(DriverSignupRequest driverSignupRequest);

    public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, Ride ride);

    public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude);

    public Driver getRequiredDriverProfile(String jwt) throws DriverException;

    public Ride getDriversCurrentRide(Integer driverId) throws DriverException;

    public List<Ride> getAllocatedRides(Integer driverId) throws DriverException;

    public Driver findDriverById(Integer driverId) throws DriverException;

    public List<Ride> getCompletedRides(Integer driverId) throws DriverException;
}
