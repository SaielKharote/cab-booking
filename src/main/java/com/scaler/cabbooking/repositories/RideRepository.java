package com.scaler.cabbooking.repositories;

import com.scaler.cabbooking.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Integer> {
}
