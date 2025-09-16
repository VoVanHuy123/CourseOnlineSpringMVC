/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.LessonProgressDTO;
import com.co.dtos.LessonWithStatusDTO;
import com.co.pojo.User;
import com.co.services.LessonProgressServices;
import com.co.services.LessonServices;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api")
@CrossOrigin
public class ApiLessonProgressController {
    @Autowired
    private LessonServices lessonServices;
    @Autowired
    private LessonProgressServices lpServices;
    
    @GetMapping("/secure/lesson_progress")
    public ResponseEntity<List<LessonProgressDTO>> list(@RequestParam Map<String,String> params){
        return ResponseEntity.ok(this.lpServices.getLessonProgresses(params));
    }
    
}
