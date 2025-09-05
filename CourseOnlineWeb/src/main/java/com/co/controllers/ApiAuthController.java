/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.JWT.JWTUtil;
import com.co.dtos.LoginRequestDTO;
import com.co.dtos.UserDTO;
import com.co.pojo.User;
import com.co.services.UserServices;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ACER
 */
@RestController
@RequestMapping("/api/")
@CrossOrigin
public class ApiAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserServices userServices;
    
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserDTO u) {
        try {
            if (this.userServices.authenticate(u.getUsername(), u.getPassword())) {
                String token = jwtUtil.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Sai thông tin đăng nhập");
            }
        } catch (Exception e) {
            // Bắt cả khi username không tồn tại hoặc query fail
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Sai thông tin đăng nhập");
        }
    }

    @RequestMapping("/secure/auth/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<User> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userServices.getUserByUsername(principal.getName()), HttpStatus.OK);
    }
    
    @PostMapping(path = "/auth/register", 
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@ModelAttribute UserDTO user) {
//        this.userServices.addOrUpdate(user);
//        return new ResponseEntity<>( HttpStatus.CREATED);
        try {
            this.userServices.addOrUpdate(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Có lỗi xảy ra!");
        }
    }
}
