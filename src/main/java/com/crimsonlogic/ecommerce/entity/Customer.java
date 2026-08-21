package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.entity.abstraction.User;
import com.crimsonlogic.ecommerce.enumeration.Role;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer extends User {

    @Column(name = "wallet_balance", precision = 10, scale = 2)
    private BigDecimal walletBalance = BigDecimal.ZERO;

    // ==========================================================
    // CUSTOMER - ADDRESS
    //
    // MANY CUSTOMERS <-> MANY ADDRESSES
    // ==========================================================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_addresses",

            joinColumns = @JoinColumn(
                    name = "customer_id"
            ),

            inverseJoinColumns = @JoinColumn(
                    name = "address_id"
            )
    )
    private Set<Address> addresses = new HashSet<>();


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public Customer() {
        super();
        setRole(Role.CUSTOMER);
    }


    // ==========================================================
    // GETTERS AND SETTERS
    // ==========================================================

    public BigDecimal getWalletBalance() { return walletBalance; }
    public void setWalletBalance(BigDecimal walletBalance) {
        this.walletBalance = walletBalance; }
    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }


    // ==========================================================
    // HELPER METHODS
    // ==========================================================

    public void addAddress(Address address) {

        if (address != null) {
            addresses.add(address);
        }
    }


    public void removeAddress(Address address) {

        if (address != null) {
            addresses.remove(address);
        }
    }
}