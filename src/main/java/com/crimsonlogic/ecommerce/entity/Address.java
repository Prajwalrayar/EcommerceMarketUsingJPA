package com.crimsonlogic.ecommerce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address {

    // ==========================================================
    // PRIMARY KEY
    // ==========================================================

    @Id
    @Column(
            name = "addressId",
            nullable = false,
            unique = true,
            length = 20
    )
    private String id;


    // ==========================================================
    // ADDRESS DETAILS
    // ==========================================================

    @Column(
            name = "house_number",
            nullable = false,
            length = 50
    )
    private String houseNumber;


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
    // MANY ADDRESSES -> ONE SELLER
    //
    // seller_id is stored in addresses table.
    // ==========================================================

    @ManyToOne
    @JoinColumn(
            name = "seller_id"
    )
    private Seller seller;


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public Address() {
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


    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
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


    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}