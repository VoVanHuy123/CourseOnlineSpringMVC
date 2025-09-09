/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Course;
import com.co.pojo.Enrollment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface EnrollmentRepository {
    public List<Enrollment> getEnrollments(Map<String, String> params) ;
    public Enrollment getEnrollmentById(int id) ;
    public void addOrUpdate(Enrollment e) ;
    public void delete(int id) ;
    public long countEnrollments(Map<String, String> params) ;
    public List<Course> getCoursesByUserId(int userId, Map<String, String> params);
}
