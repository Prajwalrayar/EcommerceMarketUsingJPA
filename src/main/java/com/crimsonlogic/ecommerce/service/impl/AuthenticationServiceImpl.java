package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.auth.CustomerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginResponseDTO;
import com.crimsonlogic.ecommerce.dto.auth.SellerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.entity.Address;
import com.crimsonlogic.ecommerce.entity.Admin;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.entity.abstraction.User;
import com.crimsonlogic.ecommerce.exception.DuplicateUserException;
import com.crimsonlogic.ecommerce.exception.InvalidCredentialsException;
import com.crimsonlogic.ecommerce.repository.AddressRepository;
import com.crimsonlogic.ecommerce.repository.AdminRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;
import com.crimsonlogic.ecommerce.service.AuthenticationService;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import com.crimsonlogic.ecommerce.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;
    private final AddressRepository addressRepository;

    public AuthenticationServiceImpl(CustomerRepository customerRepository,
                                     SellerRepository sellerRepository,
                                     AdminRepository adminRepository,
                                     AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.adminRepository = adminRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public LoginResponseDTO loginCustomer(LoginRequestDTO request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Email or Password."));

        if (!PasswordUtil.verifyPassword(request.getPassword(), customer.getPassword())) {
            throw new InvalidCredentialsException("Invalid Email or Password.");
        }
        return toLoginResponse(customer);
    }

    @Override
    public LoginResponseDTO loginSeller(LoginRequestDTO request) {
        Seller seller = sellerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Email or Password."));

        if (!PasswordUtil.verifyPassword(request.getPassword(), seller.getPassword())) {
            throw new InvalidCredentialsException("Invalid Email or Password.");
        }
        return toLoginResponse(seller);
    }

    @Override
    public LoginResponseDTO loginAdmin(LoginRequestDTO request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Email or Password."));

        if (!PasswordUtil.verifyPassword(request.getPassword(), admin.getPassword())) {
            throw new InvalidCredentialsException("Invalid Email or Password.");
        }
        return toLoginResponse(admin);
    }

    @Override
    public LoginResponseDTO registerCustomer(CustomerRegistrationRequestDTO request) {
        checkDuplicateEmail(request.getEmail());
        checkDuplicatePhone(request.getPhone());

        Customer customer = new Customer();
        customer.setId(generateUniqueId("CUS", customerRepository));
        customer.setName(request.getName().trim());
        customer.setEmail(request.getEmail().trim());
        customer.setPhone(request.getPhone().trim());
        customer.setPassword(PasswordUtil.encryptPassword(request.getPassword()));

        if (request.getAddress() != null) {
            Address address = new Address();
            address.setId(generateUniqueId("ADR", addressRepository));
            address.setHouseNumber(request.getAddress().getHouseNumber());
            address.setStreet(request.getAddress().getStreet());
            address.setCity(request.getAddress().getCity());
            address.setState(request.getAddress().getState());
            address.setCountry(request.getAddress().getCountry());
            address.setZipCode(request.getAddress().getZipCode());

            addressRepository.save(address);
            customer.addAddress(address);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return toLoginResponse(savedCustomer);
    }

    @Override
    public LoginResponseDTO registerSeller(SellerRegistrationRequestDTO request) {
        checkDuplicateEmail(request.getEmail());
        checkDuplicatePhone(request.getPhone());

        Seller seller = new Seller();
        seller.setId(generateUniqueId("SEL", sellerRepository));
        seller.setName(request.getName().trim());
        seller.setEmail(request.getEmail().trim());
        seller.setPhone(request.getPhone().trim());
        seller.setPassword(PasswordUtil.encryptPassword(request.getPassword()));
        seller.setShopName(request.getShopName().trim());

        if (request.getShopAddress() != null) {
            Address address = new Address();
            address.setId(generateUniqueId("ADR", addressRepository));
            // Seller addresses do not have a houseNumber in the DTO
            address.setStreet(request.getShopAddress().getStreet());
            address.setCity(request.getShopAddress().getCity());
            address.setState(request.getShopAddress().getState());
            address.setCountry(request.getShopAddress().getCountry());
            address.setZipCode(request.getShopAddress().getZipCode());

            // Save the unified address, then link it
            addressRepository.save(address);
            seller.addAddress(address);
        }

        Seller savedSeller = sellerRepository.save(seller);
        return toLoginResponse(savedSeller);
    }

    // ==========================================================
    // HELPER METHODS
    // ==========================================================

    private void checkDuplicateEmail(String email) {
        if (adminRepository.existsByEmail(email) ||
                sellerRepository.existsByEmail(email) ||
                customerRepository.existsByEmail(email)) {
            throw new DuplicateUserException("Email is already registered.");
        }
    }

    private void checkDuplicatePhone(String phone) {
        if (adminRepository.existsByPhone(phone) ||
                sellerRepository.existsByPhone(phone) ||
                customerRepository.existsByPhone(phone)) {
            throw new DuplicateUserException("Phone number is already registered.");
        }
    }

    private String generateUniqueId(String prefix, org.springframework.data.jpa.repository.JpaRepository repository) {
        String id;
        do {
            id = IdGenerator.generateId(prefix);
        } while (repository.existsById(id));
        return id;
    }

    private LoginResponseDTO toLoginResponse(User user) {
        return new LoginResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name()
        );
    }
}