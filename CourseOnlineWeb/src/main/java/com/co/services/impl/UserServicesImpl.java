/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.co.configs.CustomUserDetails;
import com.co.dtos.UserDTO;
import com.co.pojo.User;
import com.co.repositories.UserRepository;
import com.co.services.UserServices;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACER
 */
@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.userRepo.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid Username");
        }
        return new CustomUserDetails(u);
    }

    @Override
    public List<UserDTO> getUsers(Map<String, String> params) {
        List<User> users = this.userRepo.getUsers(params);
        return users.stream()
                .map(UserDTO::new) // gọi constructor để convert
                .toList();
    }

    @Override
    public UserDTO getUserById(int id) {
        User user = this.userRepo.getUserById(id);
        return new UserDTO(user);
    }

    @Override
    public void addOrUpdate(UserDTO user) {
        User u;

        if (user.getId() == null) {
            u = this.getUserByUsername(user.getUsername());
            if (u != null) {
                throw new RuntimeException("❌ Tên đăng nhập đã được sử dụng!");
            } else {
                u = new User();
                u.setCreatedAt(new Date());
            }

        } else {
            u = userRepo.getUserById(user.getId());
            if (u == null) {
                throw new IllegalArgumentException("user không tồn tại!");
            }else{
                if (user.getPassword() != null && !user.getPassword().isBlank()) {
                    
                    u.setPassword(this.passwordEncoder.encode(user.getPassword()));
                } 
            }
            
        }

        if (user.getAvatarFile() != null && !user.getAvatarFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(user.getAvatarFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar((String) res.get("secure_url"));
            } catch (IOException ex) {
                Logger.getLogger(UserServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (user.getIsVerify() != null) {
            u.setIsVerify(user.getIsVerify());
        } else {
            if ("teacher".equals(user.getRole())) {
                u.setIsVerify(Boolean.FALSE);
            } else {
                u.setIsVerify(Boolean.TRUE);
            }
        }

        u.setEmail(user.getEmail());
        u.setPhoneNumber(user.getPhoneNumber());
        u.setUsername(user.getUsername());
        u.setRole(user.getRole());
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());

        this.userRepo.addOrUpdate(u);

    }

    @Override
    public void delete(int id) {
        this.userRepo.delete(id);
    }
//    private UserDTO convertToDTO(User user) {
//        UserDTO dto = new UserDTO();
//        dto.setId(lesson.getId());
//        dto.setTitle(lesson.getTitle());
//        dto.setContent(lesson.getContent());
//        dto.setChapterId(lesson.getChapterId().getId());
//        dto.setChapterTitle(lesson.getChapterId().getTitle());
//        dto.setLessonOrder(lesson.getLessonOrder());
//        dto.setCreatedAt(lesson.getCreatedAt());
//        dto.setVideoUrl(lesson.getVideoUrl());
//        return dto;
//    }

    @Override
    public long countUsers(Map<String, String> params) {
        return this.userRepo.countUsers(params);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.userRepo.authenticate(username, password);
    }
}
