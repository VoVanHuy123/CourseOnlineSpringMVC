/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.CourseDTO;
import com.co.dtos.EnrollmentDTO;
import com.co.pojo.Course;
import com.co.services.EnrollmentServices;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ACER
 */
@Controller
@CrossOrigin
@RequestMapping("/api")
public class ApiEnrollmentController {

    @Autowired
    private EnrollmentServices enrollmentServices;
    

    @GetMapping("/secure/check_enrollment")
    public ResponseEntity<?> check(@RequestParam Map<String, String> params) {
        List<EnrollmentDTO> list = this.enrollmentServices.getEnrollments(params);

        if (list != null && !list.isEmpty()) {
            // Có enrollment
            return ResponseEntity.ok(list); // hoặc chỉ return ResponseEntity.ok().build()
        } else {
            // Không có enrollment
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enrollment not found");
        }
    }
    @GetMapping("/secure/my-course/{userId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByUser(@PathVariable("userId") Integer userId,@RequestParam Map<String, String> params) {
        List<CourseDTO> courses = this.enrollmentServices.getCoursesByUserId(userId, params);
        return ResponseEntity.ok(courses);
    }
}
