package com.scaler.cabbooking.repositories;

import com.scaler.cabbooking.models.Driver;
import com.scaler.cabbooking.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    public Driver findByEmail(String email);
    public Driver findDriverById(Integer id); // this method is implemented differently in the source code

    @Query("SELECT R FROM Ride R WHERE R.status = 'REQUESTED' AND R.driver.id =: driverId")
    List<Ride> getAllocatedRides(@Param("driverId") Integer driverId); // this method is implemented differently in the source code

    @Query("SELECT R FROM Ride R WHERE R.status = 'COMPLETED' AND R.driver.id =: driverId")
    public List<Ride> getCompletedRides(@Param("driverId")Integer driverId);

}
