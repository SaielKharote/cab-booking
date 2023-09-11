package com.scaler.cabbooking.controllers;

import com.scaler.cabbooking.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<MessageResponse> homeController() {
        MessageResponse msg = new MessageResponse("Welcome to the World of the Safest Rides in Town!");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
