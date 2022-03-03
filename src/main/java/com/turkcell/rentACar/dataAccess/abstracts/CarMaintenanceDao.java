package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.CarMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarMaintenanceDao extends JpaRepository<CarMaintenance, Integer> {


}
