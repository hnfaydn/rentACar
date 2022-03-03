package com.turkcell.rentACar.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.entities.concretes.Car;

@Repository
public interface CarDao extends JpaRepository<Car, Integer> {


    boolean existsByDailyPrice(double dailyPrice);

    boolean existsByModelYear(int modelYear);

    boolean existsByDescription(String description);

    boolean existsByBrand_BrandId(int brandId);

    boolean existsByColor_ColorId(int colorId);
    
    List<Car> findByDailyPriceLessThanEqual(double dailyPrice);
    
}
