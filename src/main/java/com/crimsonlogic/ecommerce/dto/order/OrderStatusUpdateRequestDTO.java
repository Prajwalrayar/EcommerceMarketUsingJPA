package com.crimsonlogic.ecommerce.dto.order;

import javax.validation.constraints.NotBlank;

public class OrderStatusUpdateRequestDTO {

    @NotBlank(message = "New status is required.")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}