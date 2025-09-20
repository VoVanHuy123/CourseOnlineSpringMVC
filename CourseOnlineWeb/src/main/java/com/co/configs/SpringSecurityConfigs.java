/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.co.JWT.JWTFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 *
 * @author admin
 */
@Configuration
@EnableTransactionManagement
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@ComponentScan(basePackages = {
    "com.co.controllers",
    "com.co.repositories",
    "com.co.services",
    "com.co.JWT"
})
public class SpringSecurityConfigs {
    
    @Autowired
    private JWTFilter jwtFilter;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
    
    @Bean
    public CustomAuthorizationManager verifiedTeacher() {
        return CustomAuthorizationManager.verifiedTeacher();
    }

    @Bean
    public CustomAuthorizationManager verifiedTeacherAndOwner() {
        return CustomAuthorizationManager.verifiedTeacherAndOwner();
    }
    @Bean
    public CustomAuthorizationManager verifiedStudentAndReviewOwner() {
        return CustomAuthorizationManager.verifiedStudentAndReviewOwner();
    }

    @Bean
    public CustomAuthorizationManager adminOnly() {
        return CustomAuthorizationManager.adminOnly();
    }



    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http, JWTFilter jwtFilter) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .securityMatcher("/api/**") 
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register","/api/courses","/api/courses/**","/api/payments/vnpay/return").permitAll()
                    
                .requestMatchers("/api/secure/stats/**").access(adminOnly())
                    
                .requestMatchers("/api/secure/get_ussers_of_course").access(verifiedTeacher())
                    
                .requestMatchers(HttpMethod.PUT,"/api/secure/courses/{id}").access(verifiedTeacherAndOwner())
                .requestMatchers(HttpMethod.DELETE,"/api/secure/courses/{id}").access(verifiedTeacherAndOwner())
                .requestMatchers(HttpMethod.PUT,"/api/secure/reviews/{id}").access(verifiedStudentAndReviewOwner())
                .requestMatchers(HttpMethod.DELETE,"/api/secure/reviews/{id}").access(verifiedStudentAndReviewOwner())
                
                 
                .requestMatchers(HttpMethod.GET, "/api/secure/courses/**", "/api/secure/chapters/**", "/api/secure/lessons/**").authenticated()
                    
                    
                .requestMatchers("/api/secure/courses","/api/secure/chapters","/api/secure/lessons").access(verifiedTeacher())
                .requestMatchers("/api/secure/auth/profile").authenticated()
                .requestMatchers("/api/getcourse/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔹 Web security: form login
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
            
            .securityMatcher("/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("admin")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dnzjjdg0v",
                        "api_key", "123958894742992",
                        "api_secret", "kQugdU7BMnVH5E4OYtFLvGKrHfk",
                        "secure", true));
        return cloudinary;
    }
    
    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000/","https://3ff2959c914c.ngrok-free.app/")); 
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
