package com.scaler.cabbooking.services;

import com.scaler.cabbooking.exceptions.DriverException;
import com.scaler.cabbooking.exceptions.RideException;
import com.scaler.cabbooking.helper.RideStatus;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.repositories.DriverRepository;
import com.scaler.cabbooking.repositories.RideRepository;
import com.scaler.cabbooking.requests.RideRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RideServiceImplementation implements RideService {
    private DriverService driverService;
    private RideRepository rideRepository;
    private CalculatorService calculator;
    private DriverRepository driverRepository;

//    private NotificationRepository notificationRepository;

    public RideServiceImplementation(DriverService driverService, RideRepository rideRepository,
                                     CalculatorService calculator, DriverRepository driverRepository) {
        this.driverService = driverService;
        this.rideRepository = rideRepository;
        this.calculator = calculator;
        this.driverRepository = driverRepository;
    }

    @Override
    public Ride requestRide(RideRequest rideRequest, Passenger passenger) throws DriverException {
        double pickupLatitude = rideRequest.getPickupLatitude();
        double pickupLongitude = rideRequest.getPickupLongitude();
        double destinationLatitude = rideRequest.getDestinationLatitude();
        double destinationLongitude = rideRequest.getDestinationLongitude();
        String pickupArea = rideRequest.getPickupArea();
        String destArea = rideRequest.getDestinationArea();

        Ride ride = new Ride();

        List<Driver> availableDrivers = driverService.getAvailableDrivers(pickupLatitude, pickupLongitude, ride);

        Driver nearestDriver = driverService.findNearestDriver(availableDrivers, pickupLatitude, pickupLongitude);

        if (nearestDriver != null) {
            System.out.println("duration-----before ride");

            Ride newRide = createRide(passenger, nearestDriver,
                    pickupLatitude, pickupLongitude,
                    destinationLatitude, destinationLongitude,
                    pickupArea, destArea);

            System.out.println("duration-----after ride");

            return newRide;
        }
        else {
            throw new DriverException("Driver not available..");
        }
    }

    @Override
    public Ride createRide(Passenger passenger, Driver nearestDriver, double pickupLatitude, double pickupLongitude, double destLatitude, double destLongitude, String pickupArea, String destArea) {
        Ride ride = new Ride();
        ride.setDriver(nearestDriver);
        ride.setPassenger(passenger);
        ride.setPickupLatitude(pickupLatitude);
        ride.setPickupLongitude(pickupLongitude);
        ride.setDestinationLatitude(destLatitude);
        ride.setDestinationLongitude(destLongitude);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setPickupArea(pickupArea);
        ride.setDestinationArea(destArea);

        System.out.println("-----a - " + pickupLatitude);

        return rideRepository.save(ride);
    }

    @Override
    public void acceptRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.ACCEPTED);

        Driver driver = ride.getDriver();
        driver.setCurrentRide(ride);

        Random random = new Random();
        int otp = random.nextInt(1000, 10000);
        ride.setOtp(otp);

        rideRepository.save(ride);
        driverRepository.save(driver);
    }

    @Override
    public void declineRide(Integer rideId, Integer driverId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.getDeclinedDrivers().add(driverId);

        List<Driver> availableDrivers = driverService.getAvailableDrivers(ride.getPickupLatitude(), ride.getPickupLongitude(), ride);

        Driver nearestDriver = driverService.findNearestDriver(availableDrivers, ride.getPickupLatitude(), ride.getPickupLongitude());

        ride.setDriver(nearestDriver);

        rideRepository.save(ride);
    }

    @Override
    public void startRide(Integer rideId, int otp) throws RideException {
        Ride ride = findRideById(rideId);

        if (otp != ride.getOtp()) {
            throw new RideException("Please provide valid OTP..");
        }

        ride.setStatus(RideStatus.STARTED);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    @Override
    public void completeRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());

        double distance = calculator.calculateDistance(ride.getPickupLatitude(), ride.getPickupLongitude(), ride.getDestinationLatitude(), ride.getDestinationLongitude());

        LocalDateTime start = ride.getStartTime();
        LocalDateTime end = ride.getEndTime();
        Duration duration = Duration.between(start, end);
        long durationMillis = duration.toMillis();

        System.out.println("duration-------" + durationMillis);
        double fare = calculator.calculateFare(distance);

        ride.setDistance(Math.round(distance * 100.0)/100.0);
        ride.setDuration(durationMillis);
        ride.setFare((int)Math.round(fare));

        Driver driver = ride.getDriver();
        driver.getRides().add(ride);
        driver.setCurrentRide(null);

        Integer totalRevenueOfDriver =  (int) (driver.getTotalRevenue() + Math.round(fare * 0.8));
        driver.setTotalRevenue(totalRevenueOfDriver);

        driverRepository.save(driver);
        rideRepository.save(ride);
    }

    @Override
    public void cancelRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.DECLINED);
        rideRepository.save(ride);
    }

    @Override
    public Ride findRideById(Integer rideId) throws RideException {
        Optional<Ride> ride = rideRepository.findById(rideId);
        if (ride.isPresent()) {
            return ride.get();
        }
        else {
            throw new RideException("Ride doesn't exist with ride_id : " + rideId);
        }
    }
}
