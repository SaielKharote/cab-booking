package com.scaler.cabbooking.repositories;

import com.scaler.cabbooking.models.Passenger;
import com.scaler.cabbooking.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    public Passenger findByEmail(String email);

    @Query("select R from Ride R where R.status = 'COMPLETED' and R.passenger.id =: passId")
    public List<Ride> getCompletedRides(@Param("passId") Integer passId);
}
