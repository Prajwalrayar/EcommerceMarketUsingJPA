package com.crimsonlogic.ecommerce.component;

import com.crimsonlogic.ecommerce.entity.Admin;
import com.crimsonlogic.ecommerce.repository.AdminRepository;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import com.crimsonlogic.ecommerce.util.PasswordUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class AdminDataLoader {

    private final AdminRepository adminRepository;

    public AdminDataLoader(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @PostConstruct
    public void loadAdmins() {

        // Prevent duplicate insertion by checking if the table already has data
        if (adminRepository.count() > 0) {
            System.out.println("Admins already exist in the database. Skipping data load.");
            return;
        }

        Admin admin1 = new Admin();
        admin1.setId(IdGenerator.generateId("ADM"));
        admin1.setName("ADMINISTRATOR");
        admin1.setEmail("admin@ecommerce.com");
        admin1.setPhone("9876543210");
        admin1.setPassword(PasswordUtil.encryptPassword("Admin@123"));

        Admin admin2 = new Admin();
        admin2.setId(IdGenerator.generateId("ADM"));
        admin2.setName("Naveen");
        admin2.setEmail("naveen@ecommerce.com");
        admin2.setPhone("9876543211");
        admin2.setPassword(PasswordUtil.encryptPassword("Naveen@123"));

        Admin admin3 = new Admin();
        admin3.setId(IdGenerator.generateId("ADM"));
        admin3.setName("Rahul");
        admin3.setEmail("rahul@ecommerce.com");
        admin3.setPhone("9876543212");
        admin3.setPassword(PasswordUtil.encryptPassword("Rahul@123"));

        // Save all admins to the database
        adminRepository.saveAll(Arrays.asList(admin1, admin2, admin3));

        System.out.println("==================================================");
        System.out.println("Default Admins Created Successfully.");
        System.out.println("==================================================");
    }
}