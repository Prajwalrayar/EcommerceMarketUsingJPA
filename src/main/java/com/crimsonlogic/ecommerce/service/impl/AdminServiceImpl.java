package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.admin.AdminDashboardStatsDTO;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.OrderRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public AdminServiceImpl(CustomerRepository customerRepository, SellerRepository sellerRepository,
                            ProductRepository productRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public AdminDashboardStatsDTO getDashboardStats() {
        AdminDashboardStatsDTO stats = new AdminDashboardStatsDTO();
        stats.setTotalCustomers(customerRepository.count());
        stats.setTotalSellers(sellerRepository.count());
        stats.setTotalProducts(productRepository.count());
        stats.setTotalOrders(orderRepository.count());

        // Calculate total sales across all orders
        Double totalSales = orderRepository.findAll().stream()
                .mapToDouble(order -> order.getTotalPrice() != null ? order.getTotalPrice() : 0.0)
                .sum();
        stats.setTotalPlatformSales(totalSales);

        return stats;
    }

    public String deleteUserByAdmin(String userId, String role) {
        if ("CUSTOMER".equalsIgnoreCase(role)) {
            Customer customer = customerRepository.findById(userId)
                    .orElseThrow(() -> new ValidationException("Customer not found."));
            customerRepository.delete(customer);
            return "Customer account successfully removed by admin.";
        } else if ("SELLER".equalsIgnoreCase(role)) {
            Seller seller = sellerRepository.findById(userId)
                    .orElseThrow(() -> new ValidationException("Seller not found."));
            sellerRepository.delete(seller);
            return "Seller account successfully removed by admin.";
        } else {
            throw new ValidationException("Invalid user role specified for deletion.");
        }
    }
}