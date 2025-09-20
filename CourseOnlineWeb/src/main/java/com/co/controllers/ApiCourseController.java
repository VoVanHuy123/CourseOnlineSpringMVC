/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.configs.CustomUserDetails;
import com.co.dtos.CourseDTO;
import com.co.dtos.ReviewDTO;
import com.co.services.CourseServices;
import com.co.services.LessonProgressServices;
import com.co.services.LessonServices;
import com.co.services.ReviewServices;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/api")
@CrossOrigin
public class ApiCourseController {

    @Autowired
    private CourseServices courseServices;
    @Autowired
    private  LessonServices lessonServices;
    @Autowired
    private LessonProgressServices lpServices;
    @Autowired
    private ReviewServices reviewServices;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.courseServices.getCourses(params), HttpStatus.OK);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDTO> retrive(@PathVariable("id") Integer id) {
        CourseDTO course = this.courseServices.getCourseById(id, true);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @PostMapping(path = "/secure/courses",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@ModelAttribute CourseDTO course) {
        this.courseServices.addOrUpdate(course);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo thành công");
    }
    @PutMapping(path = "/secure/courses/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@ModelAttribute CourseDTO course) {
        this.courseServices.addOrUpdate(course);
        return ResponseEntity.status(HttpStatus.OK).body("Update thành công");
    }
    
    @DeleteMapping("/secure/courses/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        this.courseServices.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/secure/courses/{id}/get_progress")
    public  ResponseEntity<?> getProgress(@PathVariable("id") Integer courseId , Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getId();
        int totalLessons = this.courseServices.getCourseById(courseId, false).getLessonsCount();
        int totalCompleteLessons = this.lpServices.findCompletedLessonIds(userId,courseId).size();
        Map<String,String> body = new HashMap<>();
        body.put("totalLessons", String.valueOf(totalLessons));
        body.put("totalCompleteLessons", String.valueOf(totalCompleteLessons));
        return ResponseEntity.ok(body);
    }
    
    @GetMapping("/courses/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> listReview(@PathVariable("id") int id){
        Map<String,String> params = new HashMap<>();
        params.put("courseId", String.valueOf(id));
        return ResponseEntity.ok(this.reviewServices.getReviews(params));
    }
    
}
