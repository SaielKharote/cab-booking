package com.scaler.cabbooking.repositories;

import com.scaler.cabbooking.models.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Integer> {
}
