/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.LessonProgressDTO;
import com.co.services.LessonProgressServices;
import com.co.services.LessonServices;
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
 * @author ADMIN
 */
@Controller
public class LessonProgressController {
    @Autowired
    private LessonProgressServices lessonProgressServices;

    @Autowired
    private LessonServices lessonServices;

    @Autowired
    private UserServices userServices;

    @GetMapping("/admin/lesson-progress")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("lessonProgresses", this.lessonProgressServices.getLessonProgresses(params));

        long totalItems = this.lessonProgressServices.countLessonProgress(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages < 1) {
            totalPages = 1;
        }

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("users", this.userServices.getUsers(null));
        model.addAttribute("lessons", this.lessonServices.getLessons(null));
        model.addAttribute("searchParams", params);

        return "lesson-progress"; 
    }

    @GetMapping("/admin/lesson-progress/create")
    public String create(Model model) {
        model.addAttribute("lessonProgress", new LessonProgressDTO());
        model.addAttribute("lessons", this.lessonServices.getLessons(null)); 
        model.addAttribute("users", this.userServices.getUsers(null)); 
        return "lesson-progress-details";
    }

    @GetMapping("/admin/lesson-progress/{lpId}")
    public String update(Model model, @PathVariable(value = "lpId") int id) {
        model.addAttribute("lessonProgress", this.lessonProgressServices.getLessonProgressById(id));
        model.addAttribute("lessons", this.lessonServices.getLessons(null)); 
        model.addAttribute("users", this.userServices.getUsers(null)); 
        return "lesson-progress-details";
    }

    @PostMapping("/admin/lesson-progress")
    public String createOrUpdate(
            @ModelAttribute("lessonProgress") @Valid LessonProgressDTO lpDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("lessons", this.lessonServices.getLessons(null)); 
            model.addAttribute("users", this.userServices.getUsers(null)); 
            return "lesson-progress-details";
        }

        this.lessonProgressServices.addOrUpdate(lpDTO);

        return "redirect:/admin/lesson-progress";
    }

    @PostMapping("/admin/lesson-progress/delete/{lpId}")
    public String delete(@PathVariable("lpId") int id) {
        this.lessonProgressServices.delete(id);
        return "redirect:/admin/lesson-progress";
    }
}
