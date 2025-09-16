/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.configs.CustomUserDetails;
import com.co.dtos.LessonDTO;
import com.co.dtos.LessonWithStatusDTO;
import com.co.pojo.User;
import com.co.services.LessonProgressServices;
import com.co.services.LessonServices;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private LessonProgressServices lpServices;

    @GetMapping("/secure/lessons/{id}")
    public ResponseEntity<LessonDTO> retrive(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.lessonServices.getLessonById(id));
    }

    @GetMapping("/secure/lessons")
    public ResponseEntity<List<LessonDTO>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.lessonServices.getLessons(params), HttpStatus.OK);
    }

    @PostMapping(path = "/secure/lessons",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@ModelAttribute LessonDTO lesson) {
        // Debug log
        System.out.println("===== DEBUG LessonDTO =====");
        System.out.println("id: " + lesson.getId());
        System.out.println("title: " + lesson.getTitle());
        System.out.println("content: " + lesson.getContent());
        System.out.println("isPublic: " + lesson.getIsPublic());
        System.out.println("lessonOrder: " + lesson.getLessonOrder());
        System.out.println("chapterId: " + lesson.getChapterId());
        System.out.println("chapterTitle: " + lesson.getChapterTitle());
        System.out.println("createdAt: " + lesson.getCreatedAt());
        System.out.println("videoUrl: " + lesson.getVideoUrl());
        this.lessonServices.addOrUpdate(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo thành công");
    }

    @PutMapping(path = "/secure/lessons",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> update(@ModelAttribute LessonDTO lesson) {
        this.lessonServices.addOrUpdate(lesson);
        return ResponseEntity.status(HttpStatus.OK).body("Update thành công");
    }

    @DeleteMapping("/secure/lessons/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        this.lessonServices.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/secure/lessons/{lessonId}/complete")
    public ResponseEntity<?> completeLesson(@PathVariable("lessonId") Integer lessonId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getId();
        System.out.println("LessonId = " + lessonId);
        this.lpServices.markComplete(userId, lessonId);
        return ResponseEntity.ok("Lesson marked as completed");
    }

    @GetMapping("/secure/getcourse/{id}/getlessons")
    public ResponseEntity<List<LessonWithStatusDTO>> getlessons(@PathVariable("id") Integer courseId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getId();
        List<LessonWithStatusDTO> lessons = this.lessonServices.getLessonsWithStatus(courseId, userId);
        return ResponseEntity.ok(lessons);
    }

}
