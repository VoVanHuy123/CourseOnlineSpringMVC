/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.CourseDTO;
import com.co.pojo.Course;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface StatsServices {
    public List<Map<String, Object>> getTeacherCourseStatsByMonthYear(Integer month, Integer year);
    public List<Map<String, Object>> getTeacherCourseStats();
    public BigDecimal getRevenueByMonth(Integer month);
    public Map<String, Object> getTeacherCourseRevenue(Integer teacherId, Integer month) ;
}
