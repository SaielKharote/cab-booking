package com.scaler.cabbooking.services;

import com.scaler.cabbooking.exceptions.DriverException;
import com.scaler.cabbooking.exceptions.RideException;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.requests.RideRequest;

public interface RideService {
    public Ride requestRide(RideRequest rideRequest, Passenger passenger) throws DriverException;
    public Ride createRide(Passenger passenger, Driver nearestDriver,
                           double pickupLatitude, double pickupLongitude,
                           double destLatitude, double destLongitude,
                           String pickupArea, String destArea);

    public void acceptRide(Integer rideId) throws RideException;

    public void declineRide(Integer rideId, Integer driverId) throws RideException;

    public void startRide(Integer rideId, int otp) throws RideException;

    public void completeRide(Integer rideId) throws RideException;

    public void cancelRide(Integer rideId) throws RideException;

    public Ride findRideById(Integer rideId) throws RideException;
}
