/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.JWT.JWTUtil;
import com.co.dtos.LoginRequestDTO;
import com.co.dtos.UserDTO;
import com.co.pojo.User;
import com.co.services.CourseServices;
import com.co.services.EnrollmentServices;
import com.co.services.LessonProgressServices;
import com.co.services.UserServices;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ACER
 */
@RestController
@RequestMapping("/api/")
@CrossOrigin
public class ApiAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CourseServices courseServices;

    @Autowired
    private LessonProgressServices lpServices;
    @Autowired
    private  EnrollmentServices enrollmentServices;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserServices userServices;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserDTO u) {
        try {
            if (this.userServices.authenticate(u.getUsername(), u.getPassword())) {
                String token = jwtUtil.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Sai thông tin đăng nhập");
            }
        } catch (Exception e) {
            // Bắt cả khi username không tồn tại hoặc query fail
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Sai thông tin đăng nhập");
        }
    }

    @RequestMapping("/secure/auth/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<User> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userServices.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @PostMapping(path = "/auth/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@ModelAttribute UserDTO user) {
//        this.userServices.addOrUpdate(user);
//        return new ResponseEntity<>( HttpStatus.CREATED);
        try {
            this.userServices.addOrUpdate(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Có lỗi xảy ra!");
        }
    }
    
    @GetMapping("/secure/get_ussers_of_course/{courseId}")
    public ResponseEntity<List<Map<String, Object>>> getUsersOfCourse(@PathVariable("courseId") int courseId) {
        List<UserDTO> users = this.enrollmentServices.getUsersByCourseId(courseId,new HashMap<>()); // danh sách user của khóa học
        int totalLessons = this.courseServices.getCourseById(courseId, false).getLessonsCount();

        List<Map<String, Object>> result = new ArrayList<>();

        for (UserDTO u : users) {
            int totalCompleteLessons = this.lpServices.findCompletedLessonIds(u.getId(), courseId).size();

            Map<String, Object> item = new HashMap<>();
            item.put("user", u);
            item.put("totalLessons", totalLessons);
            item.put("totalCompleteLessons", totalCompleteLessons);

            result.add(item);
        }

        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/secure/auth/users")
    public ResponseEntity<List<UserDTO>> list(@RequestParam Map<String,String> params){
        return ResponseEntity.ok(this.userServices.getUsers(params));
    }
    @PutMapping(path = "/secure/auth/users/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@ModelAttribute UserDTO user) {
        this.userServices.addOrUpdate(user);
        return ResponseEntity.status(HttpStatus.OK).body("Update thành công");
    }
    @DeleteMapping("/secure/auth/users/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        this.userServices.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
