/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.LessonDTO;
import com.co.services.LessonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ACER
 */
@Controller
@CrossOrigin
@RequestMapping("/api")
public class ApiLessonController {
    @Autowired
    private LessonServices lessonServices;

    @GetMapping("/secure/lessons/{id}")
    public ResponseEntity<LessonDTO> retrive(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.lessonServices.getLessonById(id));
    }

}
