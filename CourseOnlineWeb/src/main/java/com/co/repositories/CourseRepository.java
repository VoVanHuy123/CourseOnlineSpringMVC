/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Course;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface CourseRepository {
    public List<Course> getCourses(String params);
}
