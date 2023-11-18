package dev.damola.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int coordinateId;
    private String longitude;
    private String latitude;

    @OneToOne
    @JoinColumn(name = "addressId")
    private Address address;
}
