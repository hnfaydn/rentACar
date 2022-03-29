package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "corporateCustomers")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@PrimaryKeyJoinColumn(name="corporate_customer_id",referencedColumnName = "customer_id")
public class CorporateCustomer extends Customer {

	
	@Column(name = "company_name")
	private String companyName;

	@Column(name = "tax_number",unique = true,length = 10)
	private String taxNumber;

}
