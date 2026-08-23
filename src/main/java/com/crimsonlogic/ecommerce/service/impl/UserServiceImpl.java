package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.address.AddressDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressResponseDTO;
import com.crimsonlogic.ecommerce.dto.user.*;
import com.crimsonlogic.ecommerce.entity.Address;
import com.crimsonlogic.ecommerce.entity.Admin;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.exception.DuplicateUserException;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.AddressRepository;
import com.crimsonlogic.ecommerce.repository.AdminRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;
import com.crimsonlogic.ecommerce.service.UserService;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AddressRepository addressRepository;

    public UserServiceImpl(AdminRepository adminRepository, CustomerRepository customerRepository,
                           SellerRepository sellerRepository, AddressRepository addressRepository) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.addressRepository = addressRepository;
    }

    // ==========================================================
    // ADMIN
    // ==========================================================
    @Override
    public UserResponseDTO getAdminProfile(String adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ValidationException("Admin not found."));
        return mapToUserResponse(admin);
    }

    @Override
    public UserResponseDTO updateAdminPhone(String adminId, UpdatePhoneRequestDTO request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ValidationException("Admin not found."));
        checkDuplicatePhone(request.getPhone(), adminId);
        admin.setPhone(request.getPhone().trim());
        return mapToUserResponse(adminRepository.save(admin));
    }

    // ==========================================================
    // CUSTOMER
    // ==========================================================
    @Override
    public UserResponseDTO getCustomerProfile(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));
        return mapToUserResponse(customer);
    }

    @Override
    public UserResponseDTO updateCustomerProfile(String customerId, CustomerProfileUpdateRequestDTO request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));
        checkDuplicateEmail(request.getEmail(), customerId);
        checkDuplicatePhone(request.getPhone(), customerId);

        customer.setName(request.getName().trim());
        customer.setEmail(request.getEmail().trim());
        customer.setPhone(request.getPhone().trim());
        return mapToUserResponse(customerRepository.save(customer));
    }

    @Override
    public UserResponseDTO addCustomerAddress(String customerId, AddressDTO request) {
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

        addressRepository.save(address);
        customer.addAddress(address);
        return mapToUserResponse(customerRepository.save(customer));
    }

    @Override
    public void removeCustomerAddress(String customerId, String addressId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ValidationException("Address not found."));

        if (!customer.getAddresses().contains(address)) {
            throw new ValidationException("Address does not belong to this customer.");
        }
        customer.removeAddress(address);
        customerRepository.save(customer);
    }

    // ==========================================================
    // SELLER
    // ==========================================================
    @Override
    public UserResponseDTO getSellerProfile(String sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ValidationException("Seller not found."));
        return mapToUserResponse(seller);
    }

    @Override
    public UserResponseDTO updateSellerProfile(String sellerId, SellerProfileUpdateRequestDTO request) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ValidationException("Seller not found."));
        checkDuplicateEmail(request.getEmail(), sellerId);
        checkDuplicatePhone(request.getPhone(), sellerId);

        seller.setName(request.getName().trim());
        seller.setEmail(request.getEmail().trim());
        seller.setPhone(request.getPhone().trim());
        seller.setShopName(request.getShopName().trim());
        return mapToUserResponse(sellerRepository.save(seller));
    }

    @Override
    public UserResponseDTO addSellerAddress(String sellerId, @Valid AddressRequestDTO request) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ValidationException("Seller not found."));

        Address address = new Address();
        address.setId(IdGenerator.generateId("ADR"));
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());

        addressRepository.save(address);
        seller.addAddress(address);
        return mapToUserResponse(sellerRepository.save(seller));
    }

    @Override
    public void removeSellerAddress(String sellerId, String addressId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ValidationException("Seller not found."));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ValidationException("Address not found."));

        if (!seller.getAddresses().contains(address)) {
            throw new ValidationException("Address does not belong to this seller.");
        }
        seller.removeAddress(address);
        sellerRepository.save(seller);
    }

    // ==========================================================
    // HELPER METHODS
    // ==========================================================
    private void checkDuplicateEmail(String email, String currentUserId) {
        if ((adminRepository.existsByEmail(email) && !adminRepository.findByEmail(email).get().getId().equals(currentUserId)) ||
                (sellerRepository.existsByEmail(email) && !sellerRepository.findByEmail(email).get().getId().equals(currentUserId)) ||
                (customerRepository.existsByEmail(email) && !customerRepository.findByEmail(email).get().getId().equals(currentUserId))) {
            throw new DuplicateUserException("Email is already registered.");
        }
    }

    private void checkDuplicatePhone(String phone, String currentUserId) {
        if ((adminRepository.existsByPhone(phone) && !adminRepository.findByPhone(phone).get().getId().equals(currentUserId)) ||
                (sellerRepository.existsByPhone(phone) && !sellerRepository.findByPhone(phone).get().getId().equals(currentUserId)) ||
                (customerRepository.existsByPhone(phone) && !customerRepository.findByPhone(phone).get().getId().equals(currentUserId))) {
            throw new DuplicateUserException("Phone number is already registered.");
        }
    }

    private UserResponseDTO mapToUserResponse(Object userObj) {
        UserResponseDTO dto = new UserResponseDTO();
        if (userObj instanceof Admin admin) {
            dto.setId(admin.getId());
            dto.setName(admin.getName());
            dto.setEmail(admin.getEmail());
            dto.setPhone(admin.getPhone());
            dto.setRole(admin.getRole().name());
        } else if (userObj instanceof Customer customer) {
            dto.setId(customer.getId());
            dto.setName(customer.getName());
            dto.setEmail(customer.getEmail());
            dto.setPhone(customer.getPhone());
            dto.setRole(customer.getRole().name());
            dto.setWalletBalance(customer.getWalletBalance());
            dto.setAddresses(mapAddresses(customer.getAddresses()));
        } else if (userObj instanceof Seller seller) {
            dto.setId(seller.getId());
            dto.setName(seller.getName());
            dto.setEmail(seller.getEmail());
            dto.setPhone(seller.getPhone());
            dto.setRole(seller.getRole().name());
            dto.setShopName(seller.getShopName());
            dto.setAddresses(mapAddresses(seller.getAddresses()));
        }
        return dto;
    }

    private List<AddressResponseDTO> mapAddresses(Set<Address> addresses) {
        return addresses.stream().map(a -> {
            AddressResponseDTO dto = new AddressResponseDTO();
            dto.setAddressId(a.getId());
            dto.setHouseNumber(a.getHouseNumber());
            dto.setStreet(a.getStreet());
            dto.setCity(a.getCity());
            dto.setState(a.getState());
            dto.setCountry(a.getCountry());
            dto.setZipCode(a.getZipCode());
            return dto;
        }).collect(Collectors.toList());
    }
}