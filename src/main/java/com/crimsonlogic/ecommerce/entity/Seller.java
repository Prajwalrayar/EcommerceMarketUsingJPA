package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.entity.abstraction.User;
import com.crimsonlogic.ecommerce.enumeration.Role;

// Note: If you have migrated to Spring 6 / Hibernate 6, change javax to jakarta
import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sellers")
public class Seller extends User {

    // ==========================================================
    // SHOP NAME
    // ==========================================================

    @Column(
            name = "shop_name",
            nullable = false,
            length = 150
    )
    private String shopName;


    // ==========================================================
    // SELLER - ADDRESS
    //
    // MANY SELLERS -> MANY ADDRESSES
    // ==========================================================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "seller_addresses",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> addresses = new HashSet<>();


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public Seller() {
        super();
        setRole(Role.SELLER);
    }


    // ==========================================================
    // GETTERS AND SETTERS
    // ==========================================================

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    // CHANGED: getShopAddresses to getAddresses
    public Set<Address> getAddresses() {
        return addresses;
    }

    // CHANGED: setShopAddresses to setAddresses
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