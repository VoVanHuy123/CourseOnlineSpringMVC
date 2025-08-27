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
    
    @GetMapping("/courses")
    public String list(Model model,@RequestParam Map<String, String> params){
        model.addAttribute("courses", this.courseServices.getCourses(params));
        return "courses";
    }
    @GetMapping("/courses/create")
    public String list(Model model){
        model.addAttribute("course",new CourseDTO());
        return "course-details";
    }
    @GetMapping("/courses/{courseId}")
    public String update(Model model, @PathVariable(value = "courseId") int id) {
        model.addAttribute("course", this.courseServices.getCourseById(id));
        return "course-details";
    }
    @PostMapping("/courses")
    public String create(
            @ModelAttribute("course") @Valid CourseDTO courseDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "course-details"; // Trả về form để nhập lại
        }

        // Gửi sang service để xử lý logic nghiệp vụ + lưu
        courseServices.addOrUpdate(courseDTO);

            return "redirect:/courses";
    }
    
    
}
