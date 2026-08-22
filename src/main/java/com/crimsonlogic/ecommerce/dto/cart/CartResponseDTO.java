package com.crimsonlogic.ecommerce.dto.cart;

public class CartResponseDTO {

    private String cartId;
    private String productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}