package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarDao extends JpaRepository<Car, Integer> {

    boolean existsByDailyPrice(double dailyPrice);

    boolean existsByModelYear(int modelYear);

    boolean existsByDescription(String description);

    boolean existsByBrand_BrandId(int brandId);

    boolean existsByColor_ColorId(int colorId);

    List<Car> findByDailyPriceLessThanEqual(double dailyPrice);

}
