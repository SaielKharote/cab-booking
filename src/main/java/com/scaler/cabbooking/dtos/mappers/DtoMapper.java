package com.scaler.cabbooking.dtos.mappers;

import com.scaler.cabbooking.dtos.DriverDTO;
import com.scaler.cabbooking.dtos.LicenseDTO;
import com.scaler.cabbooking.dtos.PassengerDTO;
import com.scaler.cabbooking.dtos.RideDTO;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.License;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;

public class DtoMapper {
    public static DriverDTO toDriverDto(Driver driver) {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(driver.getEmail());
        driverDTO.setId(driver.getId());
        driverDTO.setName(driver.getName());
        driverDTO.setLatitude(driver.getLatitude());
        driverDTO.setLongitude(driver.getLongitude());
        driverDTO.setMobile(driver.getMobile());
        driverDTO.setRating(driver.getRating());
        driverDTO.setVehicle(driver.getVehicle());
        driverDTO.setUserRole(driver.getUserRole());

        return driverDTO;
    }

    public static PassengerDTO toPassengerDto(Passenger passenger) {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setEmail(passenger.getEmail());
        passengerDTO.setId(passenger.getId());
        passengerDTO.setMobile(passenger.getMobile());
        passengerDTO.setName(passenger.getName());

        return passengerDTO;
    }

    public static LicenseDTO toLicenseDto(License license) {
        LicenseDTO licenseDTO = new LicenseDTO();
        licenseDTO.setLicenseNumber(license.getLicenseNumber());
        licenseDTO.setLicenseExpiryDate(license.getLicenseExpiryDate());

        return licenseDTO;
    }

    public static RideDTO toRideDto(Ride ride) {
        PassengerDTO passengerDTO = toPassengerDto(ride.getPassenger());
        DriverDTO driverDTO = toDriverDto(ride.getDriver());

        RideDTO rideDTO = new RideDTO();

        rideDTO.setId(ride.getId());
        rideDTO.setPickupLatitude(ride.getPickupLatitude());
        rideDTO.setPickupLongitude(ride.getPickupLongitude());
        rideDTO.setDestinationLatitude(ride.getDestinationLatitude());
        rideDTO.setDestinationLongitude(ride.getDestinationLongitude());
        rideDTO.setDistance(ride.getDistance());
        rideDTO.setDuration(ride.getDuration());
        rideDTO.setPickupArea(ride.getPickupArea());
        rideDTO.setDestinationArea(ride.getDestinationArea());
        rideDTO.setStartTime(ride.getStartTime());
        rideDTO.setEndTime(ride.getEndTime());
        rideDTO.setStatus(ride.getStatus());
        rideDTO.setFare(ride.getFare());
//        rideDTO.setPaymentDetails(ride.getPaymentDetails());
        rideDTO.setDriver(driverDTO);
        rideDTO.setPassenger(passengerDTO);
        rideDTO.setOtp(ride.getOtp());

        return rideDTO;
    }
}
