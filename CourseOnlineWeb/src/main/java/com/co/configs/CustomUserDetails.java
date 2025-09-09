/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.configs;

import com.co.pojo.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author ACER
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 🔹 Lấy role từ entity User
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println(">>> ROLE = " + user.getRole());
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 🔹 Các trạng thái mặc định
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 🔹 Getter custom để PreAuthorize dùng được principal.isVerify
    public boolean isVerify() {
        System.out.println(">>> isVerify = " + user.getIsVerify());
        return Boolean.TRUE.equals(user.getIsVerify()); // an toàn với null
    }
    public String getRole(){
        return user.getRole();
    }

    // 🔹 Nếu cần thêm thông tin
    public int getId() {
        return user.getId();
    }

}
