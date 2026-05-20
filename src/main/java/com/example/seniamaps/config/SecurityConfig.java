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

    public SecurityConfig(
            UserDetailsService userDetailsService) {

        this.userDetailsService =
                userDetailsService;
    }

    /*
     * =====================================================
     * AUTH MANAGER
     * =====================================================
     */

    @Bean
    public AuthenticationManager authenticationManager(

            HttpSecurity http,

            PasswordEncoder passwordEncoder,

            UserDetailsService userDetailsService

    ) throws Exception {

        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(
                        AuthenticationManagerBuilder.class
                );

        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }

    /*
     * =====================================================
     * SECURITY FILTER CHAIN
     * =====================================================
     */

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                /*
                 * =============================================
                 * CSRF
                 * =============================================
                 */

                .csrf(AbstractHttpConfigurer::disable)

                /*
                 * =============================================
                 * AUTHORIZATION
                 * =============================================
                 */

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/index",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers("/api/**")
                        .authenticated()

                        .anyRequest()
                        .authenticated()
                )

                /*
                 * =============================================
                 * LOGIN
                 * =============================================
                 */

                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .defaultSuccessUrl("/home", true)

                        .failureUrl("/login?error")

                        .permitAll()
                )

                /*
                 * =============================================
                 * LOGOUT
                 * =============================================
                 */

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                )

                /*
                 * =============================================
                 * CACHE CONTROL
                 * =============================================
                 */

                .headers(headers -> headers

                        .cacheControl(cache ->
                                cache.disable()
                        )
                );

        return http.build();
    }

    /*
     * =====================================================
     * PASSWORD ENCODER
     * =====================================================
     */

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}