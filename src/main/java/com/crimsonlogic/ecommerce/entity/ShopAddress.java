package com.crimsonlogic.ecommerce.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shop_addresses")
public class ShopAddress {

    // ==========================================================
    // PRIMARY KEY
    // ==========================================================

    @Id
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            length = 20
    )
    private String id;


    // ==========================================================
    // SHOP ADDRESS
    //
    // No houseNumber here.
    // ==========================================================

    @Column(
            name = "street",
            nullable = false,
            length = 150
    )
    private String street;


    @Column(
            name = "city",
            nullable = false,
            length = 100
    )
    private String city;


    @Column(
            name = "state",
            nullable = false,
            length = 100
    )
    private String state;


    @Column(
            name = "country",
            nullable = false,
            length = 100
    )
    private String country;


    @Column(
            name = "zip_code",
            nullable = false,
            length = 10
    )
    private String zipCode;


    // ==========================================================
    // SELLER RELATIONSHIP
    //
    // MANY SHOP ADDRESSES -> ONE SELLER
    // ==========================================================

    @ManyToMany(mappedBy = "shopAddresses", fetch = FetchType.LAZY)
    private Set<Seller> sellers = new HashSet<>();


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public ShopAddress() {
    }


    // ==========================================================
    // GETTERS AND SETTERS
    // ==========================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Set<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(Set<Seller> sellers) {
        this.sellers = sellers;
    }
}