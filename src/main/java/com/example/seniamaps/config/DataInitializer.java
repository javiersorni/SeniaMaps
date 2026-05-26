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

            // 1. Comprobamos si el username está libre
            boolean usernameExists = usuarioRepository
                    .findByUsername(adminUsername)
                    .isPresent();

            // 2. Comprobamos si el email está libre en la base de datos
            // Nota: Si no tienes existsByEmail declarado, usa findByEmail(adminEmail).isPresent()
            // o simplemente usa la query manual para evitar fallos de compilación instantáneos:
            boolean emailExists = usuarioRepository.findAll().stream()
                    .anyMatch(u -> adminEmail.equalsIgnoreCase(u.getEmail()));

            // 🚀 SÓLO SE CREA SI NINGUNO DE LOS DOS DATOS ESTÁ YA EN LA BASE DE DATOS
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