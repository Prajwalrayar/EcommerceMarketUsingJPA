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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private AddressServiceImpl addressService;


    // ==========================================================
    // CUSTOMER - ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddCustomerAddressSuccessfully() {

        AddressRequestDTO request = new AddressRequestDTO();

        request.setHouseNumber("12A");
        request.setStreet("MG Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560001");

        Customer customer = new Customer();
        customer.setId("CUS001");
        customer.setAddresses(new HashSet<>());

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        String result =
                addressService.addCustomerAddress(
                        request,
                        "CUS001"
                );

        assertEquals(
                "Address successfully saved to your profile!",
                result
        );

        // Address should be saved
        verify(addressRepository).save(any(Address.class));

        // Customer should be saved after address is added
        verify(customerRepository).save(customer);

        // Address should actually be added to customer
        assertEquals(1, customer.getAddresses().size());

        Address savedAddress =
                customer.getAddresses().iterator().next();

        assertNotNull(savedAddress.getId());
        assertEquals("12A", savedAddress.getHouseNumber());
        assertEquals("MG Road", savedAddress.getStreet());
        assertEquals("Bengaluru", savedAddress.getCity());
        assertEquals("Karnataka", savedAddress.getState());
        assertEquals("India", savedAddress.getCountry());
        assertEquals("560001", savedAddress.getZipCode());
    }


    // ==========================================================
    // CUSTOMER - HOUSE NUMBER VALIDATION
    // ==========================================================

    @Test
    void shouldRejectCustomerAddressWhenHouseNumberIsNull() {

        AddressRequestDTO request = new AddressRequestDTO();

        request.setHouseNumber(null);
        request.setStreet("MG Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560001");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> addressService.addCustomerAddress(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "House number is compulsory for customers.",
                exception.getMessage()
        );

        verifyNoInteractions(
                customerRepository,
                addressRepository
        );
    }


    @Test
    void shouldRejectCustomerAddressWhenHouseNumberIsEmpty() {

        AddressRequestDTO request = new AddressRequestDTO();

        request.setHouseNumber("   ");
        request.setStreet("MG Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560001");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> addressService.addCustomerAddress(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "House number is compulsory for customers.",
                exception.getMessage()
        );

        verifyNoInteractions(
                customerRepository,
                addressRepository
        );
    }


    // ==========================================================
    // CUSTOMER - CUSTOMER NOT FOUND
    // ==========================================================

    @Test
    void shouldRejectAddCustomerAddressWhenCustomerDoesNotExist() {

        AddressRequestDTO request = new AddressRequestDTO();

        request.setHouseNumber("12A");
        request.setStreet("MG Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560001");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> addressService.addCustomerAddress(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById("CUS001");

        verifyNoInteractions(addressRepository);
    }


    // ==========================================================
    // CUSTOMER - GET ADDRESSES
    // ==========================================================

    @Test
    void shouldGetCustomerAddressesSuccessfully() {

        Customer customer = new Customer();
        customer.setId("CUS001");

        Address address1 = createAddress(
                "ADR001",
                "12A",
                "MG Road",
                "Bengaluru",
                "Karnataka",
                "India",
                "560001"
        );

        Address address2 = createAddress(
                "ADR002",
                "25B",
                "ITPL Road",
                "Bengaluru",
                "Karnataka",
                "India",
                "560066"
        );

        HashSet<Address> addresses = new HashSet<>();

        addresses.add(address1);
        addresses.add(address2);

        customer.setAddresses(addresses);

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        List<AddressResponseDTO> result =
                addressService.getCustomerAddresses("CUS001");

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(customerRepository)
                .findById("CUS001");
    }


    @Test
    void shouldRejectGetCustomerAddressesWhenCustomerDoesNotExist() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> addressService.getCustomerAddresses("CUS001")
                );

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById("CUS001");
    }


    // ==========================================================
    // SELLER - ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddSellerAddressSuccessfully() {

        AddressRequestDTO request = new AddressRequestDTO();

        request.setStreet("ITPL Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560066");

        Seller seller = new Seller();
        seller.setId("SEL001");
        seller.setAddresses(new HashSet<>());

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        String result =
                addressService.addSellerAddress(
                        request,
                        "SEL001"
                );

        assertEquals(
                "Address successfully saved to your shop profile!",
                result
        );

        verify(addressRepository)
                .save(any(Address.class));

        verify(sellerRepository)
                .save(seller);

        assertEquals(1, seller.getAddresses().size());

        Address savedAddress =
                seller.getAddresses().iterator().next();

        assertNotNull(savedAddress.getId());

        // Seller addresses always use N/A
        assertEquals(
                "N/A",
                savedAddress.getHouseNumber()
        );

        assertEquals(
                "ITPL Road",
                savedAddress.getStreet()
        );

        assertEquals(
                "Bengaluru",
                savedAddress.getCity()
        );

        assertEquals(
                "Karnataka",
                savedAddress.getState()
        );

        assertEquals(
                "India",
                savedAddress.getCountry()
        );

        assertEquals(
                "560066",
                savedAddress.getZipCode()
        );
    }


    // ==========================================================
    // SELLER - SELLER NOT FOUND
    // ==========================================================

    @Test
    void shouldRejectAddSellerAddressWhenSellerDoesNotExist() {

        AddressRequestDTO request = new AddressRequestDTO();

        request.setStreet("ITPL Road");
        request.setCity("Bengaluru");
        request.setState("Karnataka");
        request.setCountry("India");
        request.setZipCode("560066");

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> addressService.addSellerAddress(
                                request,
                                "SEL001"
                        )
                );

        assertEquals(
                "Seller not found.",
                exception.getMessage()
        );

        verify(sellerRepository)
                .findById("SEL001");

        verifyNoInteractions(addressRepository);
    }


    // ==========================================================
    // SELLER - GET ADDRESSES
    // ==========================================================

    @Test
    void shouldGetSellerAddressesSuccessfully() {

        Seller seller = new Seller();
        seller.setId("SEL001");

        Address address1 = createAddress(
                "ADR001",
                "N/A",
                "ITPL Road",
                "Bengaluru",
                "Karnataka",
                "India",
                "560066"
        );

        Address address2 = createAddress(
                "ADR002",
                "N/A",
                "Whitefield Road",
                "Bengaluru",
                "Karnataka",
                "India",
                "560066"
        );

        HashSet<Address> addresses = new HashSet<>();

        addresses.add(address1);
        addresses.add(address2);

        seller.setAddresses(addresses);

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        List<AddressResponseDTO> result =
                addressService.getSellerAddresses("SEL001");

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(sellerRepository)
                .findById("SEL001");
    }


    @Test
    void shouldRejectGetSellerAddressesWhenSellerDoesNotExist() {

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> addressService.getSellerAddresses("SEL001")
                );

        assertEquals(
                "Seller not found.",
                exception.getMessage()
        );

        verify(sellerRepository)
                .findById("SEL001");
    }


    // ==========================================================
    // HELPER METHOD
    // ==========================================================

    private Address createAddress(
            String id,
            String houseNumber,
            String street,
            String city,
            String state,
            String country,
            String zipCode) {

        Address address = new Address();

        address.setId(id);
        address.setHouseNumber(houseNumber);
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setZipCode(zipCode);

        return address;
    }
}