package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_damages")
public class CarDamage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_damage_id")
    private Integer carDamageId;

    @Column(name = "car_damage_description")
    private String damageDescription;


}
