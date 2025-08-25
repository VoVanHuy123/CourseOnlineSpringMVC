/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.User;

/**
 *
 * @author ACER
 */
public interface UserRepository {
    public User getUserByUsername (String username);
}
