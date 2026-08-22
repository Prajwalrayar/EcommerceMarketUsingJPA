package com.crimsonlogic.ecommerce.dto.order;

import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class CheckoutRequestDTO {
//    @NotBlank(message = "Delivery address is required.")
    private String addressId;

    @Valid
    private AddressRequestDTO newAddress;

    @NotBlank(message = "Payment method is required.")
    private String paymentMethod; // WALLET, UPI, CASH_ON_DELIVERY

    private String upiId; // Required if paymentMethod is UPI

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }

    public AddressRequestDTO getNewAddress() { return newAddress; }
    public void setNewAddress(AddressRequestDTO newAddress) { this.newAddress = newAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
}