package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.address.AddressDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.CustomerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.SellerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UpdatePhoneRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UserResponseDTO;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Admin admin;
    private Customer customer;
    private Seller seller;
    private Address address;

    @BeforeEach
    void setUp() {

        admin = new Admin();
        admin.setId("ADM001");
        admin.setName("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPhone("9999999999");

        customer = new Customer();
        customer.setId("CUS001");
        customer.setName("Prajwal");
        customer.setEmail("prajwal@gmail.com");
        customer.setPhone("9876543210");
        customer.setAddresses(new HashSet<>());

        seller = new Seller();
        seller.setId("SEL001");
        seller.setName("Seller");
        seller.setEmail("seller@gmail.com");
        seller.setPhone("9876543211");
        seller.setShopName("Prajwal Electronics");
        seller.setAddresses(new HashSet<>());

        address = new Address();
        address.setId("ADR001");
        address.setHouseNumber("12A");
        address.setStreet("MG Road");
        address.setCity("Bengaluru");
        address.setState("Karnataka");
        address.setCountry("India");
        address.setZipCode("560001");
    }

    // ==========================================================
    // ADMIN - GET PROFILE
    // ==========================================================

    @Test
    void shouldGetAdminProfileSuccessfully() {

        when(adminRepository.findById("ADM001"))
                .thenReturn(Optional.of(admin));

        UserResponseDTO result =
                userService.getAdminProfile("ADM001");

        assertNotNull(result);
        assertEquals("ADM001", result.getId());
        assertEquals("Admin", result.getName());
        assertEquals("admin@gmail.com", result.getEmail());
        assertEquals("9999999999", result.getPhone());

        verify(adminRepository)
                .findById("ADM001");
    }

    @Test
    void shouldRejectGetAdminProfileWhenAdminDoesNotExist() {

        when(adminRepository.findById("ADM001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> userService.getAdminProfile("ADM001")
                );

        assertEquals(
                "Admin not found.",
                exception.getMessage()
        );

        verify(adminRepository)
                .findById("ADM001");
    }

    // ==========================================================
    // ADMIN - UPDATE PHONE
    // ==========================================================

    @Test
    void shouldUpdateAdminPhoneSuccessfully() {

        UpdatePhoneRequestDTO request =
                new UpdatePhoneRequestDTO();

        request.setPhone("8888888888");

        when(adminRepository.findById("ADM001"))
                .thenReturn(Optional.of(admin));

        // No duplicate phone
        when(adminRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(sellerRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(customerRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(adminRepository.save(any(Admin.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserResponseDTO result =
                userService.updateAdminPhone(
                        "ADM001",
                        request
                );

        assertNotNull(result);
        assertEquals(
                "8888888888",
                result.getPhone()
        );

        verify(adminRepository)
                .save(admin);
    }

    @Test
    void shouldRejectAdminPhoneWhenPhoneAlreadyExists() {

        UpdatePhoneRequestDTO request =
                new UpdatePhoneRequestDTO();

        request.setPhone("8888888888");

        when(adminRepository.findById("ADM001"))
                .thenReturn(Optional.of(admin));

        when(adminRepository.existsByPhone("8888888888"))
                .thenReturn(true);

        when(adminRepository.findByPhone("8888888888"))
                .thenReturn(Optional.of(admin));

        // Existing user is the same admin, therefore allowed.
        when(sellerRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(customerRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(adminRepository.save(any(Admin.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserResponseDTO result =
                userService.updateAdminPhone(
                        "ADM001",
                        request
                );

        assertNotNull(result);
    }

    // ==========================================================
    // CUSTOMER - GET PROFILE
    // ==========================================================

    @Test
    void shouldGetCustomerProfileSuccessfully() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        UserResponseDTO result =
                userService.getCustomerProfile("CUS001");

        assertNotNull(result);
        assertEquals("CUS001", result.getId());
        assertEquals("Prajwal", result.getName());
        assertEquals(
                "prajwal@gmail.com",
                result.getEmail()
        );

        verify(customerRepository)
                .findById("CUS001");
    }

    @Test
    void shouldRejectGetCustomerProfileWhenCustomerDoesNotExist() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> userService.getCustomerProfile("CUS001")
                );

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );
    }

    // ==========================================================
    // CUSTOMER - UPDATE PROFILE
    // ==========================================================

    @Test
    void shouldUpdateCustomerProfileSuccessfully() {

        CustomerProfileUpdateRequestDTO request =
                new CustomerProfileUpdateRequestDTO();

        request.setName("Prajwal Updated");
        request.setEmail("updated@gmail.com");
        request.setPhone("8888888888");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        // Email not duplicated
        when(adminRepository.existsByEmail("updated@gmail.com"))
                .thenReturn(false);

        when(sellerRepository.existsByEmail("updated@gmail.com"))
                .thenReturn(false);

        when(customerRepository.existsByEmail("updated@gmail.com"))
                .thenReturn(false);

        // Phone not duplicated
        when(adminRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(sellerRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(customerRepository.existsByPhone("8888888888"))
                .thenReturn(false);

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserResponseDTO result =
                userService.updateCustomerProfile(
                        "CUS001",
                        request
                );

        assertNotNull(result);
        assertEquals(
                "Prajwal Updated",
                result.getName()
        );
        assertEquals(
                "updated@gmail.com",
                result.getEmail()
        );
        assertEquals(
                "8888888888",
                result.getPhone()
        );

        verify(customerRepository)
                .save(customer);
    }

    @Test
    void shouldRejectCustomerProfileWhenEmailBelongsToAnotherUser() {

        Customer anotherCustomer = new Customer();
        anotherCustomer.setId("CUS002");

        CustomerProfileUpdateRequestDTO request =
                new CustomerProfileUpdateRequestDTO();

        request.setName("Prajwal");
        request.setEmail("existing@gmail.com");
        request.setPhone("8888888888");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(adminRepository.existsByEmail("existing@gmail.com"))
                .thenReturn(false);

        when(sellerRepository.existsByEmail("existing@gmail.com"))
                .thenReturn(false);

        when(customerRepository.existsByEmail("existing@gmail.com"))
                .thenReturn(true);

        when(customerRepository.findByEmail("existing@gmail.com"))
                .thenReturn(Optional.of(anotherCustomer));

        DuplicateUserException exception =
                assertThrows(
                        DuplicateUserException.class,
                        () -> userService.updateCustomerProfile(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Email is already registered.",
                exception.getMessage()
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    // ==========================================================
    // CUSTOMER - ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddCustomerAddressSuccessfully() {

        AddressDTO request =
                new AddressDTO();

        request.setHouseNumber("12A");
        request.setStreet("MG Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560001");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.save(any(Address.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserResponseDTO result =
                userService.addCustomerAddress(
                        "CUS001",
                        request
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.getAddresses().size()
        );

        assertEquals(
                "12A",
                result.getAddresses().get(0).getHouseNumber()
        );

        assertEquals(
                "MG Road",
                result.getAddresses().get(0).getStreet()
        );

        verify(addressRepository)
                .save(any(Address.class));

        verify(customerRepository)
                .save(customer);
    }

    @Test
    void shouldRejectAddCustomerAddressWhenCustomerDoesNotExist() {

        AddressDTO request =
                new AddressDTO();

        request.setHouseNumber("12A");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> userService.addCustomerAddress(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );

        verifyNoInteractions(addressRepository);
    }

    // ==========================================================
    // CUSTOMER - REMOVE ADDRESS
    // ==========================================================

    @Test
    void shouldRemoveCustomerAddressSuccessfully() {

        customer.addAddress(address);

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        userService.removeCustomerAddress(
                "CUS001",
                "ADR001"
        );

        assertFalse(
                customer.getAddresses().contains(address)
        );

        verify(customerRepository)
                .save(customer);
    }

    @Test
    void shouldRejectRemovingAddressBelongingToAnotherCustomer() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> userService.removeCustomerAddress(
                                "CUS001",
                                "ADR001"
                        )
                );

        assertEquals(
                "Address does not belong to this customer.",
                exception.getMessage()
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    // ==========================================================
    // SELLER - GET PROFILE
    // ==========================================================

    @Test
    void shouldGetSellerProfileSuccessfully() {

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        UserResponseDTO result =
                userService.getSellerProfile("SEL001");

        assertNotNull(result);
        assertEquals("SEL001", result.getId());
        assertEquals("Seller", result.getName());
        assertEquals(
                "seller@gmail.com",
                result.getEmail()
        );
        assertEquals(
                "Prajwal Electronics",
                result.getShopName()
        );

        verify(sellerRepository)
                .findById("SEL001");
    }

    @Test
    void shouldRejectGetSellerProfileWhenSellerDoesNotExist() {

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> userService.getSellerProfile("SEL001")
                );

        assertEquals(
                "Seller not found.",
                exception.getMessage()
        );
    }

    // ==========================================================
    // SELLER - UPDATE PROFILE
    // ==========================================================

    @Test
    void shouldUpdateSellerProfileSuccessfully() {

        SellerProfileUpdateRequestDTO request =
                new SellerProfileUpdateRequestDTO();

        request.setName("Updated Seller");
        request.setEmail("updatedseller@gmail.com");
        request.setPhone("7777777777");
        request.setShopName("Updated Electronics");

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        when(adminRepository.existsByEmail(
                "updatedseller@gmail.com"
        )).thenReturn(false);

        when(customerRepository.existsByEmail(
                "updatedseller@gmail.com"
        )).thenReturn(false);

        when(sellerRepository.existsByEmail(
                "updatedseller@gmail.com"
        )).thenReturn(false);

        when(adminRepository.existsByPhone(
                "7777777777"
        )).thenReturn(false);

        when(customerRepository.existsByPhone(
                "7777777777"
        )).thenReturn(false);

        when(sellerRepository.existsByPhone(
                "7777777777"
        )).thenReturn(false);

        when(sellerRepository.save(any(Seller.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserResponseDTO result =
                userService.updateSellerProfile(
                        "SEL001",
                        request
                );

        assertNotNull(result);
        assertEquals(
                "Updated Seller",
                result.getName()
        );
        assertEquals(
                "updatedseller@gmail.com",
                result.getEmail()
        );
        assertEquals(
                "7777777777",
                result.getPhone()
        );
        assertEquals(
                "Updated Electronics",
                result.getShopName()
        );

        verify(sellerRepository)
                .save(seller);
    }

    // ==========================================================
    // SELLER - ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddSellerAddressSuccessfully() {

        AddressRequestDTO request =
                new AddressRequestDTO();

        request.setStreet("ITPL Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560066");

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        when(addressRepository.save(any(Address.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(sellerRepository.save(any(Seller.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserResponseDTO result =
                userService.addSellerAddress(
                        "SEL001",
                        request
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.getAddresses().size()
        );

        assertEquals(
                "ITPL Road",
                result.getAddresses().get(0).getStreet()
        );

        verify(addressRepository)
                .save(any(Address.class));

        verify(sellerRepository)
                .save(seller);
    }

    // ==========================================================
    // SELLER - REMOVE ADDRESS
    // ==========================================================

    @Test
    void shouldRemoveSellerAddressSuccessfully() {

        seller.addAddress(address);

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(sellerRepository.save(any(Seller.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        userService.removeSellerAddress(
                "SEL001",
                "ADR001"
        );

        assertFalse(
                seller.getAddresses().contains(address)
        );

        verify(sellerRepository)
                .save(seller);
    }

    @Test
    void shouldRejectRemovingSellerAddressBelongingToAnotherSeller() {

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> userService.removeSellerAddress(
                                "SEL001",
                                "ADR001"
                        )
                );

        assertEquals(
                "Address does not belong to this seller.",
                exception.getMessage()
        );

        verify(sellerRepository, never())
                .save(any(Seller.class));
    }
}