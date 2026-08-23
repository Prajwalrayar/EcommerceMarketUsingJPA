package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressResponseDTO;
import com.crimsonlogic.ecommerce.entity.Address;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.AddressRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;
import com.crimsonlogic.ecommerce.service.AddressService;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository; // Added for Seller logic

    public AddressServiceImpl(AddressRepository addressRepository,
                              CustomerRepository customerRepository,
                              SellerRepository sellerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
    }

    // ==========================================================
    // CUSTOMER LOGIC
    // ==========================================================

    public String addCustomerAddress(AddressRequestDTO request, String customerId) {
        // Enforce the rule: Customers MUST provide a house number
        if (request.getHouseNumber() == null || request.getHouseNumber().trim().isEmpty()) {
            throw new ValidationException("House number is compulsory for customers.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));

        Address address = new Address();
        address.setId(IdGenerator.generateId("ADR"));
        address.setHouseNumber(request.getHouseNumber());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());

        // Save the address to the database
        addressRepository.save(address);

        // Link it to the customer via the Many-to-Many table
        customer.addAddress(address);
        customerRepository.save(customer);

        return "Address successfully saved to your profile!";
    }

    public List<AddressResponseDTO> getCustomerAddresses(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));

        return customer.getAddresses().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // SELLER LOGIC
    // ==========================================================

    public String addSellerAddress(AddressRequestDTO request, String sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ValidationException("Seller not found."));

        Address address = new Address();
        address.setId(IdGenerator.generateId("ADR"));

        // Enforce the rule: Sellers do NOT provide house number, so we inject "N/A" for DB
        address.setHouseNumber("N/A");

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());

        // Save the address to the database
        addressRepository.save(address);

        // Link it to the seller via the Many-to-Many table
        seller.addAddress(address);
        sellerRepository.save(seller);

        return "Address successfully saved to your shop profile!";
    }

    public List<AddressResponseDTO> getSellerAddresses(String sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ValidationException("Seller not found."));

        return seller.getAddresses().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // UTILITY
    // ==========================================================

    private AddressResponseDTO mapToDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setAddressId(address.getId());
        dto.setHouseNumber(address.getHouseNumber());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setZipCode(address.getZipCode());
        return dto;
    }
}