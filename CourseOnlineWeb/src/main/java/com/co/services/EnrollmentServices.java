/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.CourseDTO;
import com.co.dtos.EnrollmentDTO;
import com.co.dtos.UserDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface EnrollmentServices {
    public List<EnrollmentDTO> getEnrollments(Map<String, String> params) ;
    public EnrollmentDTO getEnrollmentById(int id);
    public void addOrUpdate(EnrollmentDTO dto);
    public void delete(int id);
    public long countEnrollments(Map<String, String> params);
    public List<CourseDTO> getCoursesByUserId(int id,Map<String,String> params);
    public List<UserDTO> getUsersByCourseId(int courseId, Map<String, String> params);
}
