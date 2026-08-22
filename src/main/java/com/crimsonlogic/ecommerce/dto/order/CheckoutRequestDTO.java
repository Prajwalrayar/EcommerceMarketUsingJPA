package com.crimsonlogic.ecommerce.dto.order;

import javax.validation.constraints.NotBlank;

public class CheckoutRequestDTO {
    @NotBlank(message = "Delivery address is required.")
    private String addressId;

    @NotBlank(message = "Payment method is required.")
    private String paymentMethod; // WALLET, UPI, CASH_ON_DELIVERY

    private String upiId; // Required if paymentMethod is UPI

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
}