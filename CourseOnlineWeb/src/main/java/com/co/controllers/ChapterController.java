/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.ChapterDTO;
import com.co.dtos.CourseDTO;
import com.co.services.ChapterServices;
import jakarta.validation.Valid;
import java.util.List;
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
public class ChapterController {

    @Autowired
    private ChapterServices chapterServices;

    @GetMapping("/chapters")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("chapters", this.chapterServices.getChapters(params));
        long totalItems = this.chapterServices.countChapters(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        return "chapters";
    }

    @GetMapping("/chapters/create")
    public String list(Model model) {
        model.addAttribute("chapter", new ChapterDTO());
        return "chapter-details";
    }

    @GetMapping("/chapters/{chapterId}")
    public String update(Model model, @PathVariable(value = "chapterId") int id) {
        model.addAttribute("chapter", this.chapterServices.getChapterById(id));
        return "chapter-details";
    }

    @PostMapping("/chapters")
    public String create(
            @ModelAttribute("chapter") @Valid ChapterDTO chapterDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "chapter-details";
        }

        chapterServices.addOrUpdate(chapterDTO);

        return "redirect:/chapters";
    }

    @PostMapping("/chapters/delete/{chapterId}")
    public String delete(@PathVariable("chapterId") int id) {
        this.chapterServices.delete(id);
        return "redirect:/chapters";
    }
    
//    public countChapter(List)
}
