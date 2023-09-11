package com.scaler.cabbooking.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(PassengerException.class)
    public ResponseEntity<ErrorDetails> userExceptionHandler(PassengerException passengerException, WebRequest req) {
        ErrorDetails error = new ErrorDetails(passengerException.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DriverException.class)
    public ResponseEntity<ErrorDetails> driverExceptionHandler(DriverException driverException, WebRequest req) {
        ErrorDetails error = new ErrorDetails(driverException.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RideException.class)
    public ResponseEntity<ErrorDetails> rideExceptionHandler(RideException rideException, WebRequest req) {
        ErrorDetails error = new ErrorDetails(rideException.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessage.append(violation.getMessage() + "\n");
        }

        ErrorDetails err = new ErrorDetails(errorMessage.toString(), "Validation Error", LocalDateTime.now());

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        ErrorDetails error = new ErrorDetails(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage(), "Validation Error", LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ErrorDetails> otherExceptionHandler(Exception exception, WebRequest request) {
        ErrorDetails error = new ErrorDetails(exception.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.ACCEPTED);
    }
}
