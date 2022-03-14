package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalCarDao extends JpaRepository<RentalCar, Integer> {

    List<RentalCar> getByCar_CarId(int carId);

    List<RentalCar> findAllByCar_CarId(int carId);

    RentalCar findRentalCarByOrderedAdditionalService_OrderedAdditionalServiceId(int orderedAdditionalServiceId);

}
