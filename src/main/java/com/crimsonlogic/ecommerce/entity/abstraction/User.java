package com.crimsonlogic.ecommerce.entity.abstraction;

import com.crimsonlogic.ecommerce.enumeration.Role;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class User {

    // ==========================================================
    // PRIMARY KEY
    // ==========================================================

    @Id
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            length = 20
    )
    private String id;


    // ==========================================================
    // COMMON USER DETAILS
    // ==========================================================

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;


    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 150
    )
    private String email;


    @Column(
            name = "phone",
            nullable = false,
            unique = true,
            length = 15
    )
    private String phone;


    @Column(
            name = "password",
            nullable = false,
            length = 255
    )
    private String password;

    @Transient
    private Role role;

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    protected User() {
    }


    // ==========================================================
    // GETTERS AND SETTERS
    // ==========================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}