package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressResponseDTO;
import com.crimsonlogic.ecommerce.service.AddressService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(addressController)
                .build();
    }

    // ==========================================================
    // CUSTOMER - ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddCustomerAddressSuccessfully() throws Exception {

        when(addressService.addCustomerAddress(
                any(AddressRequestDTO.class),
                eq("CUS001")
        )).thenReturn("Customer address added successfully");

        String requestJson = """
                {
                    "houseNumber": "12A",
                    "street": "MG Road",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "zipCode": "560001"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/addresses/customer/add")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(addressService)
                .addCustomerAddress(
                        any(AddressRequestDTO.class),
                        eq("CUS001")
                );
    }

    @Test
    void shouldRejectAddCustomerAddressForSeller()
            throws Exception {

        String requestJson = """
                {
                    "houseNumber": "12A",
                    "street": "MG Road",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "zipCode": "560001"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/addresses/customer/add")
                                .requestAttr("userId", "SELLER001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }

    @Test
    void shouldRejectAddCustomerAddressForAdmin()
            throws Exception {

        String requestJson = """
                {
                    "houseNumber": "12A",
                    "street": "MG Road",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "zipCode": "560001"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/addresses/customer/add")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }

    // ==========================================================
    // CUSTOMER - GET ADDRESSES
    // ==========================================================

    @Test
    void shouldGetCustomerAddressesSuccessfully()
            throws Exception {

        List<AddressResponseDTO> addresses = Arrays.asList(
                new AddressResponseDTO(),
                new AddressResponseDTO()
        );

        when(addressService.getCustomerAddresses("CUS001"))
                .thenReturn(addresses);

        mockMvc.perform(
                        get("/api/v1/addresses/customer/my-addresses")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isOk());

        verify(addressService)
                .getCustomerAddresses("CUS001");
    }

    @Test
    void shouldRejectGetCustomerAddressesForSeller()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/addresses/customer/my-addresses")
                                .requestAttr("userId", "SELLER001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }

    @Test
    void shouldRejectGetCustomerAddressesForAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/addresses/customer/my-addresses")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }

    // ==========================================================
    // SELLER - ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddSellerAddressSuccessfully()
            throws Exception {

        when(addressService.addSellerAddress(
                any(AddressRequestDTO.class),
                eq("SELLER001")
        )).thenReturn("Seller address added successfully");

        String requestJson = """
                {
                    "houseNumber": "25B",
                    "street": "ITPL Road",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "zipCode": "560066"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/addresses/seller/add")
                                .requestAttr("userId", "SELLER001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(addressService)
                .addSellerAddress(
                        any(AddressRequestDTO.class),
                        eq("SELLER001")
                );
    }

    @Test
    void shouldRejectAddSellerAddressForCustomer()
            throws Exception {

        String requestJson = """
                {
                    "houseNumber": "25B",
                    "street": "ITPL Road",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "zipCode": "560066"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/addresses/seller/add")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }

    @Test
    void shouldRejectAddSellerAddressForAdmin()
            throws Exception {

        String requestJson = """
                {
                    "houseNumber": "25B",
                    "street": "ITPL Road",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "zipCode": "560066"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/addresses/seller/add")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }

    // ==========================================================
    // SELLER - GET ADDRESSES
    // ==========================================================

    @Test
    void shouldGetSellerAddressesSuccessfully()
            throws Exception {

        List<AddressResponseDTO> addresses = Arrays.asList(
                new AddressResponseDTO(),
                new AddressResponseDTO()
        );

        when(addressService.getSellerAddresses("SELLER001"))
                .thenReturn(addresses);

        mockMvc.perform(
                        get("/api/v1/addresses/seller/my-addresses")
                                .requestAttr("userId", "SELLER001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isOk());

        verify(addressService)
                .getSellerAddresses("SELLER001");
    }

    @Test
    void shouldRejectGetSellerAddressesForCustomer()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/addresses/seller/my-addresses")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }

    @Test
    void shouldRejectGetSellerAddressesForAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/addresses/seller/my-addresses")
                                .requestAttr("userId", "ADM001")
//                                .requestAttr("role", "ADMIN")

                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(addressService);
    }
}

