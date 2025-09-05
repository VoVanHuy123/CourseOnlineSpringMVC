/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Course;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface CourseRepository {
    public List<Course> getCourses(Map<String, String> params);
    public Course getCourseById(int id, boolean includeChapters);
    public void addOrUpdate(Course c);
    public void delete(int id);
    public long countCourses(Map<String, String> params);
}
