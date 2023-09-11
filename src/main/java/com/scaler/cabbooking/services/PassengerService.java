package com.scaler.cabbooking.services;

import com.scaler.cabbooking.exceptions.PassengerException;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;

import java.util.List;

public interface PassengerService {
    //    public Passenger createPassenger(Passenger passenger) throws PassengerException;
    public Passenger getReqPassengerProfile(String jwt) throws PassengerException;
    public Passenger findPassengerById(Integer PassId) throws PassengerException;
    public Passenger findPassengerByEmail(String email) throws PassengerException;
    public List<Ride> completedRides(Integer PassId) throws PassengerException;
}
