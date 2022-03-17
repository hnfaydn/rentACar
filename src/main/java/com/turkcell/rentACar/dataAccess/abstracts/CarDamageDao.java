package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.CarDamage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarDamageDao extends JpaRepository <CarDamage, Integer> {

    boolean existsByDamageDescription(String damageDescription);
}
