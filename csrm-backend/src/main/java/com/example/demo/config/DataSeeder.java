package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(User.Role.ROLE_ADMIN);
            admin.setStatus(User.AccountStatus.APPROVED);
            
            userRepository.save(admin);
            
            System.out.println("==================================================");
            System.out.println("INITIAL ADMIN ACCOUNT CREATED");
            System.out.println("Username: " + adminUsername);
            System.out.println("Password: [SECURELY HASHED] -> Default is 'admin123' if not overridden via ENV variables.");
            System.out.println("You can override credentials by setting:");
            System.out.println("ADMIN_USERNAME and ADMIN_PASSWORD in environment.");
            System.out.println("==================================================");
        }
    }
}
