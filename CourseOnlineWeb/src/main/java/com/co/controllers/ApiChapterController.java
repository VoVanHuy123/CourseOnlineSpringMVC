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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    @GetMapping("/secure/chapters/{id}")
    public ResponseEntity<ChapterDTO> retrive(@PathVariable("id") Integer id) {
        ChapterDTO chapter = this.chapterServices.getChapterById(id);
        if (chapter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chapter);
    }
    
    @PostMapping("/secure/chapters")
    public ResponseEntity<?> create(@RequestBody ChapterDTO chapter){
        System.out.println("DEBUG: ChapterDTO received -> " + chapter);
    System.out.println("DEBUG: courseId = " + chapter.getCourseId());
    System.out.println("DEBUG: title = " + chapter.getTitle());
    System.out.println("DEBUG: orderIndex = " + chapter.getOrderIndex());
        this.chapterServices.addOrUpdate(chapter);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo thành công");
    }
    @PutMapping("/secure/chapters")
    public ResponseEntity<?> update(@RequestBody ChapterDTO chapter){
        this.chapterServices.addOrUpdate(chapter);
        return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công");
    }
    @DeleteMapping("/secure/chapters/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        this.chapterServices.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
