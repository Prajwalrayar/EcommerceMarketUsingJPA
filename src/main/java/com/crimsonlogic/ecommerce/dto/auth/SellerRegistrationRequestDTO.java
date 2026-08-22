package com.crimsonlogic.ecommerce.dto.auth;

import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class SellerRegistrationRequestDTO {

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Enter a valid 10-digit Indian mobile number.")
    private String phone;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&*!?_+=-])[A-Za-z\\d@#$%^&*!?_+=-]{8,20}$",
            message = "Password must contain uppercase, lowercase, digit and special character.")
    private String password;

    @NotBlank(message = "Shop name is required.")
    @Size(min = 3, max = 100, message = "Shop name must be between 3 and 100 characters.")
    private String shopName;

    // Inside SellerRegistrationRequestDTO.java

    @NotNull(message = "Address is required.")
    @Valid
    private AddressRequestDTO address; // Renamed from shopAddress and reused the generic DTO



    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public AddressRequestDTO getAddress() { return address; }
    public void setAddress(AddressRequestDTO address) { this.address = address; }
}