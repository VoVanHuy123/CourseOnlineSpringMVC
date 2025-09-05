/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.CourseDTO;
import com.co.services.CourseServices;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ACER
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCourseController {
    @Autowired
    private CourseServices courseServices;
    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> list(@RequestParam Map<String,String> params){
        return new ResponseEntity<>(this.courseServices.getCourses(params),HttpStatus.OK);
    }
    
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDTO> retrive(@PathVariable("id") Integer id ){
        CourseDTO course = this.courseServices.getCourseById(id,true);
        if (course == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(course);
    }
}
