/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.ReviewDTO;
import com.co.services.CourseServices;
import com.co.services.ReviewServices;
import com.co.services.UserServices;
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
public class ReviewController {
    @Autowired
    private ReviewServices reviewServices;
    
    @Autowired
    private CourseServices courseServices;

    @Autowired
    private UserServices userServices;

    @GetMapping("/reviews")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("reviews", this.reviewServices.getReviews(params));

        long totalItems = this.reviewServices.countReviews(params);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
           if (totalPages < 1) {
            totalPages = 1;
        }

        int currentPage = params.get("page") != null
                ? Integer.parseInt(params.get("page")) : 1;

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);

        return "reviews";
    }

    @GetMapping("/reviews/create")
    public String create(Model model) {
        model.addAttribute("review", new ReviewDTO());
        model.addAttribute("courses", courseServices.getCourses(null));
        model.addAttribute("users", userServices.getUsers(null));
        return "review-details";
    }

    @GetMapping("/reviews/{reviewId}")
    public String update(Model model, @PathVariable(value = "reviewId") int id) {
        model.addAttribute("review", this.reviewServices.getReviewById(id));
        model.addAttribute("courses", courseServices.getCourses(null));
        model.addAttribute("users", userServices.getUsers(null));
        return "review-details";
    }

    @PostMapping("/reviews")
    public String save(
            @ModelAttribute("review") @Valid ReviewDTO reviewDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "review-details";
        }

        this.reviewServices.addOrUpdate(reviewDTO);

        return "redirect:/reviews";
    }

    @PostMapping("/reviews/delete/{reviewId}")
    public String delete(@PathVariable("reviewId") int id) {
        this.reviewServices.delete(id);
        return "redirect:/reviews";
    } 
}
