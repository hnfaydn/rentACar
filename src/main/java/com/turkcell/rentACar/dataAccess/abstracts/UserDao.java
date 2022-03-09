package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.core.utilities.entities.concretes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
}
