/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.dtos.UserDTO;
import com.co.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface UserRepository {
    public User getUserByUsername (String username);
    public List<User> getUsers (Map<String, String> params);
    public User getUserById (int id);
    public void addOrUpdate(User user);
    public void delete(int id);
    public long countUsers(Map<String, String> params);
}
