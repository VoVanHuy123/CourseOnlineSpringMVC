/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.EnrollmentDTO;
import com.co.services.EnrollmentServices;
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
 * @author ACER
 */
@Controller
public class EnrollmentController {
    @Autowired
    private EnrollmentServices enrollmentServices;

    @GetMapping("/admin/enrollments")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("enrollments", this.enrollmentServices.getEnrollments(params));
        long totalItems = this.enrollmentServices.countEnrollments(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        return "enrollments";
    }

    @GetMapping("/admin/enrollments/create")
    public String createForm(Model model) {
        model.addAttribute("enrollment", new EnrollmentDTO());
        return "enrollment-details";
    }

    @GetMapping("/admin/enrollments/{enrollmentId}")
    public String updateForm(Model model, @PathVariable(value = "enrollmentId") int id) {
        model.addAttribute("enrollment", this.enrollmentServices.getEnrollmentById(id));
        return "enrollment-details";
    }

    @PostMapping("/admin/enrollments")
    public String createOrUpdate(
            @ModelAttribute("enrollment") @Valid EnrollmentDTO enrollmentDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "enrollment-details";
        }

        this.enrollmentServices.addOrUpdate(enrollmentDTO);
        return "redirect:/admin/enrollments";
    }

    @PostMapping("/admin/enrollments/delete/{enrollmentId}")
    public String delete(@PathVariable("enrollmentId") int id) {
        this.enrollmentServices.delete(id);
        return "redirect:/admin/enrollments";
    }
}
