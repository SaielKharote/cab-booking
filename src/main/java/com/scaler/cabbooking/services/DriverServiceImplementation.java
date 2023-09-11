package com.scaler.cabbooking.services;

import com.scaler.cabbooking.config.JwtGenerator;
import com.scaler.cabbooking.exceptions.DriverException;
import com.scaler.cabbooking.helper.RideStatus;
import com.scaler.cabbooking.helper.UserRole;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.License;
import com.scaler.cabbooking.models.Ride;
import com.scaler.cabbooking.models.Vehicle;
import com.scaler.cabbooking.repositories.DriverRepository;
import com.scaler.cabbooking.repositories.LicenseRepository;
import com.scaler.cabbooking.repositories.RideRepository;
import com.scaler.cabbooking.repositories.VehicleRepository;
import com.scaler.cabbooking.requests.DriverSignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverServiceImplementation implements DriverService {
    private DriverRepository driverRepository;
    private CalculatorService distanceCalculator;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    private VehicleRepository vehicleRepository;
    private LicenseRepository licenseRepository;
    private RideRepository rideRepository;

    public DriverServiceImplementation(DriverRepository driverRepository,
                                       CalculatorService distanceCalculator,
                                       PasswordEncoder passwordEncoder,
                                       JwtGenerator jwtGenerator,
                                       VehicleRepository vehicleRepository,
                                       LicenseRepository licenseRepository,
                                       RideRepository rideRepository) {
        this.driverRepository = driverRepository;
        this.distanceCalculator = distanceCalculator;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.vehicleRepository = vehicleRepository;
        this.licenseRepository = licenseRepository;
        this.rideRepository = rideRepository;
    }

    @Override
    public Driver registerDriver(DriverSignupRequest driverSignupRequest) {
        License license = driverSignupRequest.getLicense();
        Vehicle vehicle = driverSignupRequest.getVehicle();

        License createdLicense = new License();

        createdLicense.setLicenseState(license.getLicenseState());
        createdLicense.setId(license.getId());
        createdLicense.setLicenseNumber(license.getLicenseNumber());
        createdLicense.setLicenseExpiryDate(license.getLicenseExpiryDate());

        License savedLicense = licenseRepository.save(createdLicense);

        Vehicle createdVehicle = new Vehicle();

        createdVehicle.setCapacity(vehicle.getCapacity());
        createdVehicle.setColor(vehicle.getColor());
        createdVehicle.setMake(vehicle.getMake());
        createdVehicle.setVehicleType(vehicle.getVehicleType());
        createdVehicle.setModel(vehicle.getModel());
        createdVehicle.setVehicleNumberPlate(vehicle.getVehicleNumberPlate());
        createdVehicle.setMakeYear(vehicle.getMakeYear());

        Vehicle savedVehicle = vehicleRepository.save(createdVehicle);

        Driver driver = new Driver();

        String encodedPassword = passwordEncoder.encode(driverSignupRequest.getPassword());

        driver.setName(driverSignupRequest.getName());
        driver.setEmail(driverSignupRequest.getEmail());
        driver.setMobile(driverSignupRequest.getMobile());
        driver.setPassword(encodedPassword);
        driver.setLicense(savedLicense);
        driver.setVehicle(savedVehicle);
        driver.setUserRole(UserRole.DRIVER);

        driver.setLatitude(driverSignupRequest.getLatitude());
        driver.setLongitude(driverSignupRequest.getLongitude());

        Driver savedDriver = driverRepository.save(driver);

        savedLicense.setDriver(savedDriver);
        savedVehicle.setDriver(savedDriver);

        licenseRepository.save(savedLicense);
        vehicleRepository.save(savedVehicle);

        return savedDriver;
    }

    @Override
    public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, Ride ride) {
        List<Driver> allDrivers = driverRepository.findAll();
        List<Driver> availableDrivers = new ArrayList<>();

        for (Driver driver : allDrivers) {
            if (driver.getCurrentRide() != null && driver.getCurrentRide().getStatus() != RideStatus.COMPLETED) {
                continue;
            }

            if (ride.getDeclinedDrivers().contains(driver.getId())) {
                System.out.println("Driver wid driver_id contains in Declined Drivers");
                continue;
            }
            double driverLatitude  = driver.getLatitude();
            double driverLongitude = driver.getLongitude();

            double distance = distanceCalculator.calculateDistance(driverLatitude, driverLongitude, pickupLatitude, pickupLongitude);

            availableDrivers.add(driver);
//            if (radius <= distance) {
//                availableDrivers.add(driver);
//            }
        }
        return availableDrivers;
    }

    @Override
    public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude) {
        double min_distance = Double.MAX_VALUE;
        Driver nearestDriver = null;

        for (Driver driver : availableDrivers) {
            double currentDistance = distanceCalculator.calculateDistance(pickupLatitude, pickupLongitude, driver.getLatitude(), driver.getLongitude());

            if (currentDistance < min_distance) {
                min_distance = currentDistance;
                nearestDriver = driver;
            }
        }

        return nearestDriver;
    }

    @Override
    public Driver getRequiredDriverProfile(String jwt) throws DriverException {
        String email = jwtGenerator.getEmailFromJwt(jwt);
        Driver driver = driverRepository.findByEmail(email);

        if (driver != null) {
            return driver;
        }
        else {
            throw new DriverException("Diver with this Email doesn't Exist..");
        }
    }

    @Override
    public Ride getDriversCurrentRide(Integer driverId) throws DriverException {
        Driver driver = driverRepository.findDriverById(driverId);
        if (driver != null) {
            return driver.getCurrentRide();
        }
        else {
            throw new DriverException("Driver with this Id doesn't exist..");
        }
    }

    @Override
    public List<Ride> getAllocatedRides(Integer driverId) throws DriverException {
        List<Ride> allocatedRides = driverRepository.getAllocatedRides(driverId);
        return allocatedRides;
    }

    @Override
    public Driver findDriverById(Integer driverId) throws DriverException {
        Driver driver = driverRepository.findDriverById(driverId);
        if (driver != null) {
            return driver;
        }
        else {
            throw new DriverException("Driver with this Id doesn't exist..");
        }
    }

    @Override
    public List<Ride> getCompletedRides(Integer driverId) throws DriverException {
        List<Ride> rides = driverRepository.getCompletedRides(driverId);
        return rides;
    }
}
