package dev.damola.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int addressId;
    private String state;
    private String country;
    private String zipCode;
    private String city;
    private String street;

    @OneToOne(mappedBy = "address")
    Coordinate coordinate;

    @ManyToOne
    @JoinColumn(name = "profileId")
    Profile profile;
}
