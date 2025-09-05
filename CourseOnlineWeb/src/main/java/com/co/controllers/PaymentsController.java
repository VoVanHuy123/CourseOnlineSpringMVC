/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.PaymentsDTO;
import com.co.services.PaymentsServices;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author ADMIN
 */
@Controller
public class PaymentsController {
    @Autowired
    private PaymentsServices paymentsServices;

    @GetMapping("/admin/payments")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("payments", paymentsServices.getPayments(params));

        long totalItems = paymentsServices.countPayments(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages < 1) {
            totalPages = 1;
        }
        int currentPage = params.get("page") != null ? Integer.parseInt(params.get("page")) : 1;

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        return "payments";
    }

    @GetMapping("/admin/payments/create")
    public String createForm(Model model) {
        model.addAttribute("payment", new PaymentsDTO());
        return "payment-details";
    }

    @GetMapping("/admin/payments/{id}")
    public String editForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("payment", paymentsServices.getPaymentById(id));
        return "payment-details";
    }

    @PostMapping("/admin/payments")
    public String createOrUpdate(@ModelAttribute("payment") @Valid PaymentsDTO dto,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "payment-details";
        }
        paymentsServices.addOrUpdate(dto);
        return "redirect:/admin/payments";
    }

    @PostMapping("/admin/payments/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        paymentsServices.delete(id);
        return "redirect:/admin/payments";
    }
}
