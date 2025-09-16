/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Course;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface StatsRepository {
    public List<Object[]> countCoursesByTeacherInMonthYear(Integer month, Integer year);
    public List<Object[]> countCoursesByTeacher();
    public BigDecimal getRevenueByMonth(Integer month);
    public List<Object[]> getCourseRevenueByTeacher(Integer teacherId, Integer month);
}
