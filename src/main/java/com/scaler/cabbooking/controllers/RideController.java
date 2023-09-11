package com.scaler.cabbooking.controllers;

import com.scaler.cabbooking.dtos.RideDTO;
import com.scaler.cabbooking.dtos.mappers.DtoMapper;
import com.scaler.cabbooking.exceptions.DriverException;
import com.scaler.cabbooking.exceptions.PassengerException;
import com.scaler.cabbooking.exceptions.RideException;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.requests.RideRequest;
import com.scaler.cabbooking.requests.StartRideRequest;
import com.scaler.cabbooking.responses.MessageResponse;
import com.scaler.cabbooking.services.DriverService;
import com.scaler.cabbooking.services.PassengerService;
import com.scaler.cabbooking.services.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {
    private final RideService rideService;
    private final DriverService driverService;
    private final PassengerService passengerService;

    public RideController(RideService rideService, DriverService driverService, PassengerService passengerService) {
        this.rideService = rideService;
        this.driverService = driverService;
        this.passengerService = passengerService;
    }

    @PostMapping("/request")
    public ResponseEntity<RideDTO> passengerRequestRideHandler(@RequestBody RideRequest rideRequest, @RequestHeader("Authorization") String jwt) throws PassengerException, DriverException {
        Passenger passenger = passengerService.getReqPassengerProfile(jwt);

        Ride ride = rideService.requestRide(rideRequest, passenger);

        RideDTO rideDTO = DtoMapper.toRideDto(ride);

        return new ResponseEntity<>(rideDTO, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/accept")
    public ResponseEntity<MessageResponse> acceptRideHandler(@PathVariable Integer rideId) throws RideException {
        rideService.acceptRide(rideId);
        MessageResponse msg = new MessageResponse("Ride accepted by Driver!!");
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/decline")
    public ResponseEntity<MessageResponse> declineRideHandler(@RequestHeader("Authorization") String jwt ,@PathVariable Integer rideId) throws DriverException, RideException {
        Driver driver = driverService.getRequiredDriverProfile(jwt);
        rideService.declineRide(rideId, driver.getId());
        MessageResponse msg = new MessageResponse("Ride declined by Driver !!");
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/start")
    public ResponseEntity<MessageResponse> startRideRequestHandler(@RequestBody StartRideRequest request, @PathVariable Integer rideId) throws RideException {
        rideService.startRide(rideId, request.getOtp());
        MessageResponse msg = new MessageResponse("Ride started.. Have a safe ride..");
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<MessageResponse> completeRideHandler(@PathVariable Integer rideId) throws RideException {
        rideService.completeRide(rideId);
        MessageResponse msg = new MessageResponse("Ride completed.. Thanks for trusting us!!");
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideDTO> findRideById(@RequestHeader("Authorization") String jwt ,@PathVariable Integer rideId) throws RideException {
//        Passenger passenger = passengerService.getReqPassengerProfile(jwt);
        Ride ride = rideService.findRideById(rideId);
        RideDTO rideDTO = DtoMapper.toRideDto(ride);

        return new ResponseEntity<>(rideDTO, HttpStatus.ACCEPTED);
    }
}
