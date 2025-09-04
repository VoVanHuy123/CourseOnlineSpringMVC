/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.UserDTO;
import com.co.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author ACER
 */
public interface UserServices extends UserDetailsService{
    User getUserByUsername(String username);
    public List<UserDTO> getUsers (Map<String, String> params);
    public UserDTO getUserById (int id);
    public void addOrUpdate(UserDTO user);
    public void delete(int id);
    public long countUsers(Map<String, String> params);
    public boolean authenticate(String username, String password);
}
