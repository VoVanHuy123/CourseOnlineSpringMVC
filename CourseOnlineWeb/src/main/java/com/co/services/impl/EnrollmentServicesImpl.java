/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.co.dtos.CourseDTO;
import com.co.dtos.EnrollmentDTO;
import com.co.pojo.Course;
import com.co.pojo.Enrollment;
import com.co.pojo.User;
import com.co.repositories.CourseRepository;
import com.co.repositories.EnrollmentRepository;
import com.co.repositories.UserRepository;
import com.co.services.EnrollmentServices;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACER
 */
@Service
//@Transactional
public class EnrollmentServicesImpl implements  EnrollmentServices{
    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<EnrollmentDTO> getEnrollments(Map<String, String> params) {
        List<Enrollment> enrollments = this.enrollmentRepo.getEnrollments(params);
        return enrollments.stream()
                .map(EnrollmentDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDTO getEnrollmentById(int id) {
        Enrollment e = this.enrollmentRepo.getEnrollmentById(id);
        if (e == null) return null;
        return new EnrollmentDTO(e);
    }

    @Override
    public void addOrUpdate(EnrollmentDTO dto) {
        Enrollment e;

        if (dto.getId() == null) {
            e = new Enrollment();
            e.setEnrolledAt(new Date());
            e.setStatus("pending");
        } else {
            e = this.enrollmentRepo.getEnrollmentById(dto.getId());
            if (e == null) {
                throw new IllegalArgumentException("Enrollment không tồn tại!");
            }
        }

        // Map dữ liệu từ DTO → Entity
        if (dto.getCourseId() != null) {
            Course c = this.courseRepo.getCourseById(dto.getCourseId(),false);
            if (c == null) {
                throw new IllegalArgumentException("Course không tồn tại!");
            }
            e.setCourseId(c);
        }

        if (dto.getUserId() != null) {
            User u = this.userRepo.getUserById(dto.getUserId());
            if (u == null) {
                throw new IllegalArgumentException("User không tồn tại!");
            }
            e.setUserId(u);
        }
        
        if (dto.getStatus() != null) {  
            e.setStatus(dto.getStatus());
        }

        this.enrollmentRepo.addOrUpdate(e);
    }

    @Override
    public void delete(int id) {
        this.enrollmentRepo.delete(id);
    }

    @Override
    public long countEnrollments(Map<String, String> params) {
        return this.enrollmentRepo.countEnrollments(params);
    }

    @Override
    public List<CourseDTO> getCoursesByUserId(int id, Map<String,String> params) {
        List<Course> courses = this.enrollmentRepo.getCoursesByUserId(id,params);
        return  courses.stream()
                .map(CourseDTO::new)
                .toList();
    }
}
