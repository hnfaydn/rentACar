package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarDao extends JpaRepository<Car, Integer> {

    boolean existsCarByBrand_BrandIdAndColor_ColorIdAndDailyPriceAndModelYearAndDescription(int brandId,int colorId,double dailyPrice,int modelYear,String description);

    List<Car> findByDailyPriceLessThanEqual(double dailyPrice);
}
