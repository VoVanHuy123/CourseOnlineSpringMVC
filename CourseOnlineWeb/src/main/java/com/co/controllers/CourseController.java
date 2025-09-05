/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.CourseDTO;
import com.co.pojo.Course;
import com.co.services.CourseServices;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class CourseController {

    @Autowired
    private CourseServices courseServices;

    @GetMapping("/admin/courses")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("courses", this.courseServices.getCourses(params));
        long totalItems = this.courseServices.countCourses(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;
        
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        return "courses";
    }

    @GetMapping("/admin/courses/create")
    public String list(Model model) {
        model.addAttribute("course", new CourseDTO());
        return "course-details";
    }

    @GetMapping("/admin/courses/{courseId}")
    public String update(Model model, @PathVariable(value = "courseId") int id) {
        model.addAttribute("course", this.courseServices.getCourseById(id,false));
        return "course-details";
    }

    @PostMapping("/admin/courses")
    public String create(
            @ModelAttribute("course") @Valid CourseDTO courseDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "course-details"; 
        }

        
        courseServices.addOrUpdate(courseDTO);

        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/courses/delete/{courseId}")
    public String delete(@PathVariable("courseId") int id) {
        this.courseServices.delete(id);
        return "redirect:/admin/courses";
    }

}
