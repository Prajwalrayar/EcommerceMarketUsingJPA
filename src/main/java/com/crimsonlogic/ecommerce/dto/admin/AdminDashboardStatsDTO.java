package com.crimsonlogic.ecommerce.dto.admin;

public class AdminDashboardStatsDTO {
    private long totalCustomers;
    private long totalSellers;
    private long totalProducts;
    private long totalOrders;
    private Double totalPlatformSales;

    // Getters and Setters
    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
    public long getTotalSellers() { return totalSellers; }
    public void setTotalSellers(long totalSellers) { this.totalSellers = totalSellers; }
    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public Double getTotalPlatformSales() { return totalPlatformSales; }
    public void setTotalPlatformSales(Double totalPlatformSales) { this.totalPlatformSales = totalPlatformSales; }
}