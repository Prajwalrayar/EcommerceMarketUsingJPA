package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.entity.abstraction.User;
import com.crimsonlogic.ecommerce.enumeration.Role;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public Admin() {
        super();
        setRole(Role.ADMIN);
    }
}