package com.scaler.cabbooking.controllers;

import com.scaler.cabbooking.exceptions.PassengerException;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.services.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {
    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping("/{passId}")
    public ResponseEntity<Passenger> findPassengerByIdHandler(@PathVariable Integer passId) throws PassengerException {
        Passenger passenger = passengerService.findPassengerById(passId);
        return new ResponseEntity<>(passenger, HttpStatus.ACCEPTED);
    }

    @GetMapping("/profile")
    public ResponseEntity<Passenger> getReqPassengerProfileHandler(@RequestHeader("Authorization") String jwt) throws PassengerException {
        Passenger passenger = passengerService.getReqPassengerProfile(jwt);
        return new ResponseEntity<>(passenger, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/completed")
    public ResponseEntity<List<Ride>> getCompletedRidesHandler(@RequestHeader("Authorization") String jwt) throws PassengerException {
        Passenger passenger = passengerService.getReqPassengerProfile(jwt);
        List<Ride> rides = passengerService.completedRides(passenger.getId());
        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }
}
