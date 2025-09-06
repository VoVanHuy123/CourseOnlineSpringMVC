/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.ChapterDTO;
import com.co.services.ChapterServices;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ACER
 */
@Controller
@RequestMapping("/api")
@CrossOrigin
public class ApiChapterController {
    @Autowired
    private ChapterServices chapterServices;
    
    @GetMapping("/secure/chapters")
    public ResponseEntity<List<ChapterDTO>> list(@RequestParam Map<String,String> params){
        return  new ResponseEntity<>(this.chapterServices.getChapters(params),HttpStatus.OK);
    }
}
