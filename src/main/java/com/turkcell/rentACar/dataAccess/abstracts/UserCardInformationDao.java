package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.UserCardInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardInformationDao extends JpaRepository<UserCardInformation, Integer> {
}
