package com.scaler.cabbooking.repositories;

import com.scaler.cabbooking.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
