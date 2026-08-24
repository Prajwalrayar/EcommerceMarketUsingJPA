package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.address.AddressDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.CustomerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.SellerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UpdatePhoneRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UserResponseDTO;
import com.crimsonlogic.ecommerce.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserProfileController userProfileController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(userProfileController)
                .build();
    }


    // ==========================================================
    // ADMIN PROFILE
    // ==========================================================

    @Test
    void shouldGetAdminProfileSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.getAdminProfile("ADM001"))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/admin/profile")
                                .requestAttr("userId", "ADM001")
                )
                .andExpect(status().isOk());

        verify(userService)
                .getAdminProfile("ADM001");
    }


    // ==========================================================
    // ADMIN UPDATE PHONE
    // ==========================================================

    @Test
    void shouldUpdateAdminPhoneSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.updateAdminPhone(
                eq("ADM001"),
                any(UpdatePhoneRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "phone": "9876543210"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/admin/profile/phone")
                                .requestAttr("userId", "ADM001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(userService)
                .updateAdminPhone(
                        eq("ADM001"),
                        any(UpdatePhoneRequestDTO.class)
                );
    }


    // ==========================================================
    // CUSTOMER PROFILE
    // ==========================================================

    @Test
    void shouldGetCustomerProfileSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.getCustomerProfile("CUS001"))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/customer/profile")
                                .requestAttr("userId", "CUS001")
                )
                .andExpect(status().isOk());

        verify(userService)
                .getCustomerProfile("CUS001");
    }


    // ==========================================================
    // CUSTOMER UPDATE PROFILE
    // ==========================================================

    @Test
    void shouldUpdateCustomerProfileSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.updateCustomerProfile(
                eq("CUS001"),
                any(CustomerProfileUpdateRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "name": "Prajwal",
                    "email": "prajwal@gmail.com",
                    "phone": "9876543210"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/customer/profile")
                                .requestAttr("userId", "CUS001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(userService)
                .updateCustomerProfile(
                        eq("CUS001"),
                        any(CustomerProfileUpdateRequestDTO.class)
                );
    }


    // ==========================================================
    // CUSTOMER ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddCustomerAddressSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.addCustomerAddress(
                eq("CUS001"),
                any(AddressDTO.class)
        )).thenReturn(response);

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
                        post("/api/v1/customer/address")
                                .requestAttr("userId", "CUS001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(userService)
                .addCustomerAddress(
                        eq("CUS001"),
                        any(AddressDTO.class)
                );
    }


    // ==========================================================
    // CUSTOMER REMOVE ADDRESS
    // ==========================================================

    @Test
    void shouldRemoveCustomerAddressSuccessfully()
            throws Exception {

        mockMvc.perform(
                        delete("/api/v1/customer/address/ADDR001")
                                .requestAttr("userId", "CUS001")
                )
                .andExpect(status().isOk());

        verify(userService)
                .removeCustomerAddress(
                        "CUS001",
                        "ADDR001"
                );
    }


    // ==========================================================
    // SELLER PROFILE
    // ==========================================================

    @Test
    void shouldGetSellerProfileSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.getSellerProfile("SEL001"))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/seller/profile")
                                .requestAttr("userId", "SEL001")
                )
                .andExpect(status().isOk());

        verify(userService)
                .getSellerProfile("SEL001");
    }


    // ==========================================================
    // SELLER UPDATE PROFILE
    // ==========================================================

    @Test
    void shouldUpdateSellerProfileSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.updateSellerProfile(
                eq("SEL001"),
                any(SellerProfileUpdateRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "name": "Prajwal",
                    "email": "seller@gmail.com",
                    "phone": "9876543211",
                    "shopName": "Sham Electronics"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/seller/profile")
                                .requestAttr("userId", "SEL001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(userService)
                .updateSellerProfile(
                        eq("SEL001"),
                        any(SellerProfileUpdateRequestDTO.class)
                );
    }


    // ==========================================================
    // SELLER ADD ADDRESS
    // ==========================================================

    @Test
    void shouldAddSellerAddressSuccessfully()
            throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.addSellerAddress(
                eq("SEL001"),
                any(AddressRequestDTO.class)
        )).thenReturn(response);

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
                        post("/api/v1/seller/address")
                                .requestAttr("userId", "SEL001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(userService)
                .addSellerAddress(
                        eq("SEL001"),
                        any(AddressRequestDTO.class)
                );
    }


    // ==========================================================
    // SELLER REMOVE ADDRESS
    // ==========================================================

    @Test
    void shouldRemoveSellerAddressSuccessfully()
            throws Exception {

        mockMvc.perform(
                        delete("/api/v1/seller/address/ADDR001")
                                .requestAttr("userId", "SEL001")
                )
                .andExpect(status().isOk());

        verify(userService)
                .removeSellerAddress(
                        "SEL001",
                        "ADDR001"
                );
    }
}