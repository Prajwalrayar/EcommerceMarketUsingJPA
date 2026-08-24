package com.crimsonlogic.ecommerce.dto.user;

import com.crimsonlogic.ecommerce.dto.address.AddressResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String role;

    // Optional fields depending on the Role
    private BigDecimal walletBalance;
    private String shopName;
    private List<AddressResponseDTO> addresses;

    // Standard Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public BigDecimal getWalletBalance() { return walletBalance; }
    public void setWalletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public List<AddressResponseDTO> getAddresses() { return addresses; }
    public void setAddresses(List<AddressResponseDTO> addresses) { this.addresses = addresses; }
}