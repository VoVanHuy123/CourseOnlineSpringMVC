/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.CourseDTO;
import com.co.pojo.Course;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface CourseServices {
    public List<CourseDTO> getCourses(Map<String, String> params);
    public CourseDTO getCourseById(int id ,boolean includeChapters);
    public void addOrUpdate(CourseDTO c);
    public void delete(int id);
    public long countCourses(Map<String, String> params);
}