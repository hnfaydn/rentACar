package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Integer> {

    List<Invoice> findInvoicesByRentalCar_RentalCarId(int id);

}
