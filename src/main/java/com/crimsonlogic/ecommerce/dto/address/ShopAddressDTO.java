package com.crimsonlogic.ecommerce.dto.address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ShopAddressDTO {
    @NotBlank(message = "Street is required.")
    @Size(max = 150, message = "Street is too long.")
    private String street;

    @NotBlank(message = "City is required.")
    @Size(max = 50, message = "City is too long.")
    private String city;

    @NotBlank(message = "State is required.")
    @Size(max = 50, message = "State is too long.")
    private String state;

    @NotBlank(message = "Country is required.")
    @Size(max = 50, message = "Country is too long.")
    private String country;

    @NotBlank(message = "ZIP code is required.")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Enter a valid 6-digit Indian ZIP code.")
    private String zipCode;

    // Getters and Setters
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}