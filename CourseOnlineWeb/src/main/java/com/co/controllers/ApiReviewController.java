/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.ReviewDTO;
import com.co.services.ReviewServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ACER
 */
@RestController
@RequestMapping("/api/")
@CrossOrigin
public class ApiReviewController {
    @Autowired
    private ReviewServices reviewServices;
    
    @PostMapping("/secure/reviews")
    public ResponseEntity<?> create(@RequestBody ReviewDTO review){
        ReviewDTO newReview = this.reviewServices.addOrUpdateReview(review);
        return ResponseEntity.ok(newReview);
    }
    @PutMapping("/secure/reviews/{id}")
    public ResponseEntity<?> update(@RequestBody ReviewDTO review){
        this.reviewServices.addOrUpdate(review);
        return ResponseEntity.ok("Cập nhật thành công");
    }
    @DeleteMapping("/secure/reviews/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        this.reviewServices.delete(id);
        return ResponseEntity.noContent().build();
    }
}
