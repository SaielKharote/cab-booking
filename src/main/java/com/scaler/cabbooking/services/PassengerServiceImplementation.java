package com.scaler.cabbooking.services;

import com.scaler.cabbooking.config.JwtGenerator;
import com.scaler.cabbooking.exceptions.PassengerException;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.repositories.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImplementation implements PassengerService {
    private PassengerRepository passengerRepository;
    private JwtGenerator jwtGenerator;

    public PassengerServiceImplementation(PassengerRepository passengerRepository, JwtGenerator jwtGenerator) {
        this.passengerRepository = passengerRepository;
        this.jwtGenerator = jwtGenerator;
    }

//    @Override
//    public Passenger createPassenger(Passenger passenger) throws PassengerException {
//        return null;
//    }

    @Override
    public Passenger getReqPassengerProfile(String jwt) throws PassengerException {
        String email = jwtGenerator.getEmailFromJwt(jwt);
        Passenger passenger = passengerRepository.findByEmail(email);

        if (passenger != null) {
            return passenger;
        }
        else {
            throw new PassengerException("Invalid JWT token..");
        }
    }

    @Override
    public Passenger findPassengerById(Integer passId) throws PassengerException {
        Passenger passenger = findPassengerById(passId);
        if (passenger != null) {
            return passenger;
        }
        else {
            throw new PassengerException("Passenger with Id : "+passId+" doesn't exist..");
        }
    }

    @Override
    public Passenger findPassengerByEmail(String email) throws PassengerException {
        Passenger passenger = findPassengerByEmail(email);
        if (passenger != null) {
            return passenger;
        }
        else {
            throw new PassengerException("Passenger with Email : "+email+" doesn't exist..");
        }
    }

    @Override
    public List<Ride> completedRides(Integer passId) throws PassengerException {
        List<Ride> completedRides = passengerRepository.getCompletedRides(passId);
        return completedRides;
    }
}
