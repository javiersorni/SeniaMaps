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
                http.getSharedObject(AuthenticationManagerBuilder.class);

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                /*
                 * =============================================
                 * AUTHORIZATION RULES
                 * =============================================
                 */
                .authorizeHttpRequests(auth -> auth

                        // públicos
                        .requestMatchers(
                                "/",
                                "/index",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // SOLO ADMIN
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // API solo autenticados
                        .requestMatchers("/api/**")
                        .authenticated()

                        // todo lo demás autenticado
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

                        // 🔥 REDIRECCIÓN INTELIGENTE
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
                 * HEADERS
                 * =============================================
                 */
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable())
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