package com.scaler.cabbooking.controllers;

import com.scaler.cabbooking.exceptions.DriverException;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.services.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/drivers")
public class DriverController {
    private DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Driver> getReqDriverProfileHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getRequiredDriverProfile(jwt);
        return new ResponseEntity<>(driver, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/current")
    public ResponseEntity<Ride> getDriversCurrentRideHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getRequiredDriverProfile(jwt);
        Ride ride = driverService.getDriversCurrentRide(driver.getId());
        return new ResponseEntity<>(ride, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/allocated")
    public ResponseEntity<List<Ride>> getAllocatedRidesHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getRequiredDriverProfile(jwt);
        List<Ride> allocatedRides = driverService.getAllocatedRides(driver.getId());
        return new ResponseEntity<>(allocatedRides, HttpStatus.ACCEPTED);
    }


    @GetMapping("/rides/completed")
    public ResponseEntity<List<Ride>> getCompletedRidesHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getRequiredDriverProfile(jwt);
        List<Ride> completedRides = driverService.getCompletedRides(driver.getId());
        return new ResponseEntity<>(completedRides, HttpStatus.ACCEPTED);
    }


    @GetMapping("/{driverId}/find_driver")
    public ResponseEntity<Driver> findDriverById(@PathVariable Integer driverId) throws DriverException {
        Driver driver = driverService.findDriverById(driverId);
        return new ResponseEntity<>(driver, HttpStatus.ACCEPTED);
    }
}
