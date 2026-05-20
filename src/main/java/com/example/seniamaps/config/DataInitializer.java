package com.example.seniamaps.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String adminUsername = "admin";

            boolean exists = usuarioRepository
                    .findByUsername(adminUsername)
                    .isPresent();

            if (!exists) {

                Usuario admin = new Usuario();

                admin.setUsername(adminUsername);
                admin.setEmail("admin@sistema.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol("ROLE_ADMIN");

                usuarioRepository.save(admin);

                System.out.println("🟢 ADMIN CREADO AUTOMÁTICAMENTE");
                System.out.println("👤 user: admin");
                System.out.println("🔑 pass: admin123");
            } else {
                System.out.println("🟡 ADMIN YA EXISTE");
            }
        };
    }
}