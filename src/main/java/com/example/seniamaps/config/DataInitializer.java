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
            String adminEmail = "admin@sistema.com";

            //Check if username is free.
            boolean usernameExists = usuarioRepository
                    .findByUsername(adminUsername)
                    .isPresent();

            // We check if email exist.
            boolean emailExists = usuarioRepository.findAll().stream()
                    .anyMatch(u -> adminEmail.equalsIgnoreCase(u.getEmail()));

            // Create user only if email and username are free.
            if (!usernameExists && !emailExists) {

                Usuario admin = new Usuario();

                admin.setUsername(adminUsername);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol("ROLE_ADMIN");

                usuarioRepository.save(admin);

                System.out.println("🟢 ADMIN CREADO AUTOMÁTICAMENTE");
                System.out.println("👤 user: admin");
                System.out.println("🔑 pass: admin123");
            } else {
                System.out.println("🟡 EL ADMINISTRADOR O SU CORREO YA EXISTEN EN LA BASE DE DATOS. NO SE CREA DUPLICADO.");
            }
        };
    }
}