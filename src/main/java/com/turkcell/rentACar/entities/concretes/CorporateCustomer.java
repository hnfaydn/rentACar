package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "corporateCustomers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@PrimaryKeyJoinColumn(name="corporate_customer_id",referencedColumnName = "customer_id")
public class CorporateCustomer extends Customer {

	@Column(name = "corporate_customer_id", insertable = false , updatable = false)
	private int corporateCustomerId;
	
	@Column(name = "company_name")
	private String companyName;

	@Column(name = "tax_number")
	private String taxNumber;

}
