/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.QnaDTO;
import com.co.services.QnaServices;
import com.co.services.UserServices;
import com.co.services.LessonServices;
import jakarta.validation.Valid;
import java.util.HashMap;
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

    @Autowired
    private UserServices userServices;

    @Autowired
    private LessonServices lessonServices;

    @GetMapping("/admin/qnas")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int pageSize = 8;

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;

        params.put("page", String.valueOf(currentPage));
        params.put("pageSize", String.valueOf(pageSize));

        model.addAttribute("qnas", this.qnaServices.getQnas(params));

        long totalItems = this.qnaServices.countQnas(params);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);

        return "qna-list";
    }

    @GetMapping("/admin/qnas/create")
    public String createForm(Model model) {
        model.addAttribute("qna", new QnaDTO());
        model.addAttribute("users", userServices.getUsers(new HashMap<>()));
        model.addAttribute("lessons", lessonServices.getLessons(new HashMap<>()));
        return "qna-details"; 
    }

    @GetMapping("/admin/qnas/{id}")
    public String updateForm(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("qna", this.qnaServices.getQnaById(id));
        model.addAttribute("users", userServices.getUsers(new HashMap<>()));
        model.addAttribute("lessons", lessonServices.getLessons(new HashMap<>()));
        return "qna-details";  
    }

    @PostMapping("/admin/qnas")
    public String save(@ModelAttribute("qna") @Valid QnaDTO dto,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors() || dto.getUserId() == null || dto.getLessonId() == null) {
            model.addAttribute("users", userServices.getUsers(new HashMap<>()));
            model.addAttribute("lessons", lessonServices.getLessons(new HashMap<>()));
            model.addAttribute("error", "Vui lòng chọn User và Bài học!");
            return "qna-details";  
        }
        this.qnaServices.addOrUpdate(dto);
        return "redirect:/admin/qnas";  
    }

    @GetMapping("/admin/qnas/delete/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        this.qnaServices.delete(id);
        return "redirect:/admin/qnas";  
    }
}
