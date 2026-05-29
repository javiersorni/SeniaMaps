package com.example.seniamaps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

        //AUTH MANAGER

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {

        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }

    
     //SECURITY FILTER CHAIN
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                //AUTHORIZATION RULES

                .authorizeHttpRequests(auth -> auth

                        // public
                        .requestMatchers(
                                "/",
                                "/index",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/Logo_azul_degradado_FV_2lineas_FV_logo_v3_2lineas.svg",
                                "/cropped-CENTRADO-NEGRO-150x150.jpg"
                        ).permitAll()

                        // only admin
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // API for only autenticated users
                        .requestMatchers("/api/**")
                        .authenticated()

                        // the rest.
                        .anyRequest()
                        .authenticated()
                )

                //Login
                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        // Smart redirect
                        .successHandler((request, response, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a ->
                                            a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect("/admin/home");
                            } else {
                                response.sendRedirect("/home");
                            }
                        })

                        .failureUrl("/login?error")

                        .permitAll()
                )

                //Logout
                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                )

                //header
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable())
                );

        return http.build();
    }

    //Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}