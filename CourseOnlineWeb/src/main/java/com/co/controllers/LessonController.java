/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.LessonDTO;
import com.co.services.LessonServices;
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
public class LessonController {
    
    @Autowired
    private LessonServices lessonServices;
    @GetMapping("/lessons")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("lessons", this.lessonServices.getLessons(params));
        long totalItems = this.lessonServices.countLessons(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        return "lessons";
    }
    @GetMapping("/lessons/create")
    public String list(Model model) {
        model.addAttribute("lesson", new LessonDTO());
        return "lesson-details";
    }
    @GetMapping("/lessons/{lessonId}")
    public String update(Model model, @PathVariable(value = "lessonId") int id) {
        model.addAttribute("lesson", this.lessonServices.getLessonById(id));
        return "lesson-details";
    }
    @PostMapping("/lessons")
    public String create(
            @ModelAttribute("lesson") @Valid LessonDTO chapterDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "lesson-details";
        }

        this.lessonServices.addOrUpdate(chapterDTO);

        return "redirect:/lessons";
    }
    
    @PostMapping("/lessons/delete/{lessonId}")
    public String delete(@PathVariable("lessonId") int id) {
        this.lessonServices.delete(id);
        return "redirect:/lessons";
    }
}
