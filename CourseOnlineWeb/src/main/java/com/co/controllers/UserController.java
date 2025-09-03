/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.LessonDTO;
import com.co.dtos.UserDTO;
import com.co.services.UserServices;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class UserController {
    @Autowired
    private UserServices userServices;
    
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }
    @GetMapping("/admin/users")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("users", this.userServices.getUsers(params));
        long totalItems = this.userServices.countUsers(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        return "users";
    }
    @GetMapping("/admin/users/create")
    public String list(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user-details";
    }
    @GetMapping("/admin/users/{usersId}")
    public String update(Model model, @PathVariable(value = "usersId") int id) {
        model.addAttribute("user", this.userServices.getUserById(id));
        return "user-details";
    }
    @PostMapping("/admin/users")
    public String create(
            @ModelAttribute("user") @Valid UserDTO userDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "user-details";
        }

        this.userServices.addOrUpdate(userDTO);

        return "redirect:/admin/users";
    }
    
    @PostMapping("/admin/users/delete/{userId}")
    public String delete(@PathVariable("userId") int id) {
        this.userServices.delete(id);
        return "redirect:/users";
    }
}
