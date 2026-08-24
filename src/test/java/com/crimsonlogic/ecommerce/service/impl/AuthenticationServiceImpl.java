package com.crimsonlogic.ecommerce.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.CustomerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginResponseDTO;
import com.crimsonlogic.ecommerce.dto.auth.SellerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.entity.Address;
import com.crimsonlogic.ecommerce.entity.Admin;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.exception.DuplicateUserException;
import com.crimsonlogic.ecommerce.exception.InvalidCredentialsException;
import com.crimsonlogic.ecommerce.repository.AddressRepository;
import com.crimsonlogic.ecommerce.repository.AdminRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    // ==========================================================
    // MOCK REPOSITORIES
    // ==========================================================

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AddressRepository addressRepository;


    // ==========================================================
    // SERVICE UNDER TEST
    // ==========================================================

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;


    // ==========================================================
    // CUSTOMER LOGIN - SUCCESS
    // ==========================================================

    @Test
    void shouldLoginCustomerSuccessfully() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("customer@gmail.com");
        request.setPassword("Password@123");


        Customer customer = new Customer();

        customer.setId("CUS001");
        customer.setName("Prajwal");
        customer.setEmail("customer@gmail.com");
        customer.setPhone("9876543210");
        customer.setPassword("encrypted-password");


        when(customerRepository.findByEmail("customer@gmail.com"))
                .thenReturn(Optional.of(customer));


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class)) {

            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.verifyPassword(
                            "Password@123",
                            "encrypted-password"
                    )
            ).thenReturn(true);


            LoginResponseDTO result =
                    authenticationService.loginCustomer(request);


            assertNotNull(result);

            assertEquals(
                    "CUS001",
                    result.getId()
            );

            assertEquals(
                    "Prajwal",
                    result.getName()
            );

            assertEquals(
                    "customer@gmail.com",
                    result.getEmail()
            );

            assertEquals(
                    "9876543210",
                    result.getPhone()
            );

            assertEquals(
                    "CUSTOMER",
                    result.getRole()
            );


            verify(customerRepository)
                    .findByEmail("customer@gmail.com");
        }
    }


    // ==========================================================
    // CUSTOMER LOGIN - EMAIL NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenCustomerEmailDoesNotExist() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("unknown@gmail.com");
        request.setPassword("Password@123");


        when(customerRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());


        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () -> authenticationService.loginCustomer(request)
                );


        assertEquals(
                "Invalid Email or Password.",
                exception.getMessage()
        );


        verify(customerRepository)
                .findByEmail("unknown@gmail.com");
    }


    // ==========================================================
    // CUSTOMER LOGIN - WRONG PASSWORD
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenCustomerPasswordIsWrong() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("customer@gmail.com");
        request.setPassword("WrongPassword");


        Customer customer = new Customer();

        customer.setId("CUS001");
        customer.setName("Prajwal");
        customer.setEmail("customer@gmail.com");
        customer.setPassword("encrypted-password");


        when(customerRepository.findByEmail("customer@gmail.com"))
                .thenReturn(Optional.of(customer));


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class)) {

            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.verifyPassword(
                            "WrongPassword",
                            "encrypted-password"
                    )
            ).thenReturn(false);


            InvalidCredentialsException exception =
                    assertThrows(
                            InvalidCredentialsException.class,
                            () -> authenticationService.loginCustomer(request)
                    );


            assertEquals(
                    "Invalid Email or Password.",
                    exception.getMessage()
            );
        }
    }


    // ==========================================================
    // SELLER LOGIN - SUCCESS
    // ==========================================================

    @Test
    void shouldLoginSellerSuccessfully() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("seller@gmail.com");
        request.setPassword("Password@123");


        Seller seller = new Seller();

        seller.setId("SEL001");
        seller.setName("Prajwal");
        seller.setEmail("seller@gmail.com");
        seller.setPhone("9876543211");
        seller.setPassword("encrypted-password");


        when(sellerRepository.findByEmail("seller@gmail.com"))
                .thenReturn(Optional.of(seller));


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class)) {

            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.verifyPassword(
                            "Password@123",
                            "encrypted-password"
                    )
            ).thenReturn(true);


            LoginResponseDTO result =
                    authenticationService.loginSeller(request);


            assertNotNull(result);

            assertEquals("SEL001", result.getId());
            assertEquals("Prajwal", result.getName());
            assertEquals("seller@gmail.com", result.getEmail());
            assertEquals("9876543211", result.getPhone());
            assertEquals("SELLER", result.getRole());


            verify(sellerRepository)
                    .findByEmail("seller@gmail.com");
        }
    }


    // ==========================================================
    // SELLER LOGIN - EMAIL NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenSellerEmailDoesNotExist() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("unknown@gmail.com");
        request.setPassword("Password@123");


        when(sellerRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());


        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () -> authenticationService.loginSeller(request)
                );


        assertEquals(
                "Invalid Email or Password.",
                exception.getMessage()
        );


        verify(sellerRepository)
                .findByEmail("unknown@gmail.com");
    }


    // ==========================================================
    // SELLER LOGIN - WRONG PASSWORD
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenSellerPasswordIsWrong() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("seller@gmail.com");
        request.setPassword("WrongPassword");


        Seller seller = new Seller();

        seller.setId("SEL001");
        seller.setName("Prajwal");
        seller.setEmail("seller@gmail.com");
        seller.setPassword("encrypted-password");


        when(sellerRepository.findByEmail("seller@gmail.com"))
                .thenReturn(Optional.of(seller));


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class)) {

            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.verifyPassword(
                            "WrongPassword",
                            "encrypted-password"
                    )
            ).thenReturn(false);


            InvalidCredentialsException exception =
                    assertThrows(
                            InvalidCredentialsException.class,
                            () -> authenticationService.loginSeller(request)
                    );


            assertEquals(
                    "Invalid Email or Password.",
                    exception.getMessage()
            );
        }
    }


    // ==========================================================
    // ADMIN LOGIN - SUCCESS
    // ==========================================================

    @Test
    void shouldLoginAdminSuccessfully() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("admin@gmail.com");
        request.setPassword("Admin@123");


        Admin admin = new Admin();

        admin.setId("ADM001");
        admin.setName("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPhone("9876543212");
        admin.setPassword("encrypted-password");


        when(adminRepository.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.of(admin));


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class)) {

            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.verifyPassword(
                            "Admin@123",
                            "encrypted-password"
                    )
            ).thenReturn(true);


            LoginResponseDTO result =
                    authenticationService.loginAdmin(request);


            assertNotNull(result);

            assertEquals("ADM001", result.getId());
            assertEquals("Admin", result.getName());
            assertEquals("admin@gmail.com", result.getEmail());
            assertEquals("9876543212", result.getPhone());
            assertEquals("ADMIN", result.getRole());


            verify(adminRepository)
                    .findByEmail("admin@gmail.com");
        }
    }


    // ==========================================================
    // ADMIN LOGIN - EMAIL NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenAdminEmailDoesNotExist() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("unknown@gmail.com");
        request.setPassword("Admin@123");


        when(adminRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());


        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () -> authenticationService.loginAdmin(request)
                );


        assertEquals(
                "Invalid Email or Password.",
                exception.getMessage()
        );


        verify(adminRepository)
                .findByEmail("unknown@gmail.com");
    }


    // ==========================================================
    // ADMIN LOGIN - WRONG PASSWORD
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenAdminPasswordIsWrong() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("admin@gmail.com");
        request.setPassword("WrongPassword");


        Admin admin = new Admin();

        admin.setId("ADM001");
        admin.setName("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("encrypted-password");


        when(adminRepository.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.of(admin));


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class)) {

            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.verifyPassword(
                            "WrongPassword",
                            "encrypted-password"
                    )
            ).thenReturn(false);


            InvalidCredentialsException exception =
                    assertThrows(
                            InvalidCredentialsException.class,
                            () -> authenticationService.loginAdmin(request)
                    );


            assertEquals(
                    "Invalid Email or Password.",
                    exception.getMessage()
            );
        }
    }


    // ==========================================================
    // CUSTOMER REGISTRATION - SUCCESS
    // ==========================================================

    @Test
    void shouldRegisterCustomerSuccessfully() {

        CustomerRegistrationRequestDTO request =
                new CustomerRegistrationRequestDTO();

        request.setName(" Prajwal ");
        request.setEmail(" customer@gmail.com ");
        request.setPhone(" 9876543210 ");
        request.setPassword("Password@123");


        // No duplicate email
        when(adminRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(customerRepository.existsByEmail(anyString()))
                .thenReturn(false);


        // No duplicate phone
        when(adminRepository.existsByPhone(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsByPhone(anyString()))
                .thenReturn(false);

        when(customerRepository.existsByPhone(anyString()))
                .thenReturn(false);


        // ID does not already exist
        when(customerRepository.existsById(anyString()))
                .thenReturn(false);


        Customer savedCustomer = new Customer();

        savedCustomer.setId("CUS001");
        savedCustomer.setName("Prajwal");
        savedCustomer.setEmail("customer@gmail.com");
        savedCustomer.setPhone("9876543210");


        when(customerRepository.save(any(Customer.class)))
                .thenReturn(savedCustomer);


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class);

             MockedStatic<com.crimsonlogic.ecommerce.util.IdGenerator> idGenerator =
                     mockStatic(com.crimsonlogic.ecommerce.util.IdGenerator.class)) {


            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.encryptPassword(
                            "Password@123"
                    )
            ).thenReturn("encrypted-password");


            idGenerator.when(() ->
                    com.crimsonlogic.ecommerce.util.IdGenerator.generateId("CUS")
            ).thenReturn("CUS001");


            LoginResponseDTO result =
                    authenticationService.registerCustomer(request);


            assertNotNull(result);

            assertEquals(
                    "CUS001",
                    result.getId()
            );

            assertEquals(
                    "Prajwal",
                    result.getName()
            );

            assertEquals(
                    "customer@gmail.com",
                    result.getEmail()
            );

            assertEquals(
                    "9876543210",
                    result.getPhone()
            );

            assertEquals(
                    "CUSTOMER",
                    result.getRole()
            );


            verify(customerRepository)
                    .save(any(Customer.class));
        }
    }


    // ==========================================================
    // CUSTOMER REGISTRATION - DUPLICATE EMAIL
    // ==========================================================

    @Test
    void shouldRejectCustomerRegistrationWhenEmailAlreadyExists() {

        CustomerRegistrationRequestDTO request =
                new CustomerRegistrationRequestDTO();

        request.setName("Prajwal");
        request.setEmail("customer@gmail.com");
        request.setPhone("9876543210");
        request.setPassword("Password@123");


        when(adminRepository.existsByEmail("customer@gmail.com"))
                .thenReturn(true);


        DuplicateUserException exception =
                assertThrows(
                        DuplicateUserException.class,
                        () -> authenticationService.registerCustomer(request)
                );


        assertEquals(
                "Email is already registered.",
                exception.getMessage()
        );


        verify(customerRepository, never())
                .save(any(Customer.class));
    }


    // ==========================================================
    // CUSTOMER REGISTRATION - DUPLICATE PHONE
    // ==========================================================

    @Test
    void shouldRejectCustomerRegistrationWhenPhoneAlreadyExists() {

        CustomerRegistrationRequestDTO request =
                new CustomerRegistrationRequestDTO();

        request.setName("Prajwal");
        request.setEmail("customer@gmail.com");
        request.setPhone("9876543210");
        request.setPassword("Password@123");


        when(adminRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(customerRepository.existsByEmail(anyString()))
                .thenReturn(false);


        when(adminRepository.existsByPhone("9876543210"))
                .thenReturn(true);


        DuplicateUserException exception =
                assertThrows(
                        DuplicateUserException.class,
                        () -> authenticationService.registerCustomer(request)
                );


        assertEquals(
                "Phone number is already registered.",
                exception.getMessage()
        );


        verify(customerRepository, never())
                .save(any(Customer.class));
    }


    // ==========================================================
    // SELLER REGISTRATION - SUCCESS
    // ==========================================================

    @Test
    void shouldRegisterSellerSuccessfully() {

        SellerRegistrationRequestDTO request =
                new SellerRegistrationRequestDTO();

        request.setName(" Prajwal ");
        request.setEmail(" seller@gmail.com ");
        request.setPhone(" 9876543211 ");
        request.setPassword("Password@123");
        request.setShopName(" Sham Electronics ");


        AddressRequestDTO address =
                new AddressRequestDTO();

        address.setStreet("ITPL Road");
        address.setCity("Bengaluru");
        address.setState("Karnataka");
        address.setCountry("India");
        address.setZipCode("560066");

        request.setAddress(address);


        // Duplicate checks
        when(adminRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(customerRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(adminRepository.existsByPhone(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsByPhone(anyString()))
                .thenReturn(false);

        when(customerRepository.existsByPhone(anyString()))
                .thenReturn(false);


        // IDs
        when(sellerRepository.existsById(anyString()))
                .thenReturn(false);

        when(addressRepository.existsById(anyString()))
                .thenReturn(false);


        Seller savedSeller = new Seller();

        savedSeller.setId("SEL001");
        savedSeller.setName("Prajwal");
        savedSeller.setEmail("seller@gmail.com");
        savedSeller.setPhone("9876543211");
        savedSeller.setShopName("Sham Electronics");


        when(sellerRepository.save(any(Seller.class)))
                .thenReturn(savedSeller);


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class);

             MockedStatic<com.crimsonlogic.ecommerce.util.IdGenerator> idGenerator =
                     mockStatic(com.crimsonlogic.ecommerce.util.IdGenerator.class)) {


            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.encryptPassword(
                            "Password@123"
                    )
            ).thenReturn("encrypted-password");


            idGenerator.when(() ->
                    com.crimsonlogic.ecommerce.util.IdGenerator.generateId("SEL")
            ).thenReturn("SEL001");

            idGenerator.when(() ->
                    com.crimsonlogic.ecommerce.util.IdGenerator.generateId("ADR")
            ).thenReturn("ADR001");


            LoginResponseDTO result =
                    authenticationService.registerSeller(request);


            assertNotNull(result);

            assertEquals(
                    "SEL001",
                    result.getId()
            );

            assertEquals(
                    "Prajwal",
                    result.getName()
            );

            assertEquals(
                    "seller@gmail.com",
                    result.getEmail()
            );

            assertEquals(
                    "9876543211",
                    result.getPhone()
            );

            assertEquals(
                    "SELLER",
                    result.getRole()
            );


            verify(addressRepository)
                    .save(any(Address.class));

            verify(sellerRepository)
                    .save(any(Seller.class));
        }
    }


    // ==========================================================
    // SELLER REGISTRATION - DUPLICATE EMAIL
    // ==========================================================

    @Test
    void shouldRejectSellerRegistrationWhenEmailAlreadyExists() {

        SellerRegistrationRequestDTO request =
                new SellerRegistrationRequestDTO();

        request.setName("Prajwal");
        request.setEmail("seller@gmail.com");
        request.setPhone("9876543211");
        request.setPassword("Password@123");
        request.setShopName("Sham Electronics");


        when(customerRepository.existsByEmail("seller@gmail.com"))
                .thenReturn(true);


        DuplicateUserException exception =
                assertThrows(
                        DuplicateUserException.class,
                        () -> authenticationService.registerSeller(request)
                );


        assertEquals(
                "Email is already registered.",
                exception.getMessage()
        );


        verify(sellerRepository, never())
                .save(any(Seller.class));
    }


    // ==========================================================
    // SELLER REGISTRATION - ADDRESS IS NULL
    // ==========================================================

    @Test
    void shouldRegisterSellerWithoutAddressAtServiceLevel() {

        SellerRegistrationRequestDTO request =
                new SellerRegistrationRequestDTO();

        request.setName("Prajwal");
        request.setEmail("seller@gmail.com");
        request.setPhone("9876543211");
        request.setPassword("Password@123");
        request.setShopName("Sham Electronics");

        /*
         * Important:
         *
         * Your service currently checks:
         *
         * if (request.getAddress() != null)
         *
         * Therefore, at the SERVICE level, a null address
         * is allowed.
         *
         * @NotNull validation belongs to the controller/DTO
         * validation layer.
         */


        when(adminRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(customerRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(adminRepository.existsByPhone(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsByPhone(anyString()))
                .thenReturn(false);

        when(customerRepository.existsByPhone(anyString()))
                .thenReturn(false);

        when(sellerRepository.existsById(anyString()))
                .thenReturn(false);


        Seller savedSeller = new Seller();

        savedSeller.setId("SEL001");
        savedSeller.setName("Prajwal");
        savedSeller.setEmail("seller@gmail.com");
        savedSeller.setPhone("9876543211");
        savedSeller.setShopName("Sham Electronics");


        when(sellerRepository.save(any(Seller.class)))
                .thenReturn(savedSeller);


        try (MockedStatic<com.crimsonlogic.ecommerce.util.PasswordUtil> passwordUtil =
                     mockStatic(com.crimsonlogic.ecommerce.util.PasswordUtil.class);

             MockedStatic<com.crimsonlogic.ecommerce.util.IdGenerator> idGenerator =
                     mockStatic(com.crimsonlogic.ecommerce.util.IdGenerator.class)) {


            passwordUtil.when(() ->
                    com.crimsonlogic.ecommerce.util.PasswordUtil.encryptPassword(
                            "Password@123"
                    )
            ).thenReturn("encrypted-password");


            idGenerator.when(() ->
                    com.crimsonlogic.ecommerce.util.IdGenerator.generateId("SEL")
            ).thenReturn("SEL001");


            LoginResponseDTO result =
                    authenticationService.registerSeller(request);


            assertNotNull(result);

            assertEquals(
                    "SEL001",
                    result.getId()
            );

            verify(sellerRepository)
                    .save(any(Seller.class));

            verify(addressRepository, never())
                    .save(any(Address.class));
        }
    }
}
