package com.crimsonlogic.ecommerce.dto.inventory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class InventoryUpdateRequestDTO {

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 0, message = "Stock quantity cannot be negative.")
    private Integer quantity;

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}