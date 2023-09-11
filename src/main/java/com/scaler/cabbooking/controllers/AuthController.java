package com.scaler.cabbooking.controllers;

import com.scaler.cabbooking.config.JwtGenerator;
import com.scaler.cabbooking.exceptions.PassengerException;
import com.scaler.cabbooking.helper.UserRole;
import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.repositories.DriverRepository;
import com.scaler.cabbooking.repositories.PassengerRepository;
import com.scaler.cabbooking.requests.DriverSignupRequest;
import com.scaler.cabbooking.requests.LoginRequest;
import com.scaler.cabbooking.requests.SignupRequest;
import com.scaler.cabbooking.responses.JwtResponse;
import com.scaler.cabbooking.services.CustomUserDetailsService;
import com.scaler.cabbooking.services.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private DriverRepository driverRepository;
    private PassengerRepository passengerRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    private CustomUserDetailsService customUserDetailsService;
    private DriverService driverService;

    public AuthController(DriverRepository driverRepository, PassengerRepository passengerRepository,
                          PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, DriverService driverService,
                          CustomUserDetailsService customUserDetailsService) {
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.driverService = driverService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/passenger/signup")
    public ResponseEntity<JwtResponse> signupPassenger(@RequestBody SignupRequest req) throws PassengerException {
        String email = req.getEmail();
        String name = req.getName();
        String mobile = req.getMobile();
        String password = req.getPassword();

        Passenger passenger = passengerRepository.findByEmail(email);

        if (passenger != null) {
            throw new PassengerException("User Already Exist with " + email);
        }

        String encodedPassword = passwordEncoder.encode(password);

        Passenger createdPassenger = new Passenger();
        createdPassenger.setEmail(email);
        createdPassenger.setMobile(mobile);
        createdPassenger.setName(name);
        createdPassenger.setPassword(encodedPassword);
        createdPassenger.setUserRole(UserRole.PASSENGER);

        Passenger savedPassenger = passengerRepository.save(createdPassenger);

        Authentication authentication = new UsernamePasswordAuthenticationToken(createdPassenger.getEmail(), createdPassenger.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtGenerator.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.PASSENGER);
        jwtResponse.setMessage("Account created successfully for " + savedPassenger.getName() + " ..");

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/passenger/signin")
    public ResponseEntity<JwtResponse> signinPassenger(@RequestBody LoginRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtGenerator.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.PASSENGER);
        jwtResponse.setMessage("Logged in successfully as Passenger..");

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/driver/signin")
    public ResponseEntity<JwtResponse> signinDriver(@RequestBody LoginRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtGenerator.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.DRIVER);
        jwtResponse.setMessage("Logged in successfully as Driver..");

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.ACCEPTED);
    }


    @PostMapping("/driver/signup")
    public ResponseEntity<JwtResponse> signupDriver(@RequestBody DriverSignupRequest request) {
        Driver driver = driverRepository.findByEmail(request.getEmail());
        JwtResponse jwtResponse = new JwtResponse();

        if (driver != null) {
            jwtResponse.setAuthenticated(false);
            jwtResponse.setErrorDetails("This email has been already registered...");
            jwtResponse.setError(true);

            return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.BAD_REQUEST);
        }

        Driver createdDriver = driverService.registerDriver(request);

        Authentication authentication = new UsernamePasswordAuthenticationToken(createdDriver.getEmail(), createdDriver.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtGenerator.generateJwtToken(authentication);

        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.DRIVER);
        jwtResponse.setMessage("Account successfully created for " + createdDriver.getName());

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.ACCEPTED);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Username or Password from Authenticate Method");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Username or Password..");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
    }
}
