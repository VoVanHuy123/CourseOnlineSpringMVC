/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.QnaDTO;
import com.co.services.QnaServices;
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
public class QnaController {

    @Autowired
    private QnaServices qnaServices;

    @GetMapping("/qnas")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("qnas", this.qnaServices.getQnas(params));
        return "qna-list"; 
    }

    @GetMapping("/qnas/create")
    public String createForm(Model model) {
        model.addAttribute("qna", new QnaDTO());
        return "qna-details"; 
    }

    @GetMapping("/qnas/{id}")
    public String updateForm(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("qna", this.qnaServices.getQnaById(id));
        return "qna-details";  
    }

    
    @PostMapping("/qnas")
    public String save(@ModelAttribute("qna") @Valid QnaDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return "qna-details";  
        }
        this.qnaServices.addOrUpdate(dto);
        return "redirect:/qnas";  
    }

    
    @GetMapping("/qnas/delete/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        this.qnaServices.delete(id);
        return "redirect:/qnas";  
    }
}
