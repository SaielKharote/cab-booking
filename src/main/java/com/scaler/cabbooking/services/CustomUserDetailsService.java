package com.scaler.cabbooking.services;

import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.repositories.DriverRepository;
import com.scaler.cabbooking.repositories.PassengerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private DriverRepository driverRepository;
    private PassengerRepository passengerRepository;

    public CustomUserDetailsService(DriverRepository driverRepository, PassengerRepository passengerRepository) {
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Passenger passenger = passengerRepository.findByEmail(username);

        if (passenger != null) {
            return new User(passenger.getEmail(), passenger.getPassword(), authorities);
        }

        Driver driver = driverRepository.findByEmail(username);

        if (driver != null) {
            return new User(driver.getEmail(), driver.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("user with email : " + username + " not found!");
    }
}
