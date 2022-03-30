package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "colors")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private int colorId;

    @Column(name = "color_name")
    private String name;

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Car> cars;

}
