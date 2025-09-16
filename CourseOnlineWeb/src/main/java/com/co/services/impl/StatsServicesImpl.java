/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.co.dtos.CourseDTO;
import com.co.pojo.Course;
import com.co.pojo.User;
import com.co.repositories.StatsRepository;
import com.co.services.StatsServices;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACER
 */
@Service
public class StatsServicesImpl implements StatsServices{
    @Autowired
    private StatsRepository statsRepo;

    @Override
public List<Map<String, Object>> getTeacherCourseStatsByMonthYear(Integer month, Integer year) {
    List<Object[]> results = this.statsRepo.countCoursesByTeacherInMonthYear(month, year);
    List<Map<String, Object>> response = new ArrayList<>();

    for (Object[] r : results) {
        User teacher = (User) r[0];  // root.get("teacherId")
        Long count = (Long) r[1];    // cb.count(root)

        Map<String, Object> item = new HashMap<>();
        item.put("teacher", teacher.getFullName());
        item.put("teacherId", teacher.getId());
        item.put("courseCount", count);
        item.put("month", month);
        item.put("year", year);
        response.add(item);
    }

    return response;
}

    @Override
    public List<Map<String, Object>> getTeacherCourseStats() {
        List<Object[]> results = this.statsRepo.countCoursesByTeacher();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] r : results) {
            User teacher = (User) r[0];
            Long publicCount = (Long) r[1];
            Long notPublicCount = (Long) r[2];

            Map<String, Object> item = new HashMap<>();
            item.put("teacher", teacher.getFullName());
            item.put("coursePublic", publicCount);
            item.put("courseNotPublicYet", notPublicCount);
            response.add(item);
        }
        return response;
    }

    @Override
    public BigDecimal getRevenueByMonth(Integer month) {
        return this.statsRepo.getRevenueByMonth(month);
    }

    @Override
    public Map<String, Object> getTeacherCourseRevenue(Integer teacherId, Integer month) {
        List<Object[]> results = this.statsRepo.getCourseRevenueByTeacher(teacherId, month);
        BigDecimal total = BigDecimal.ZERO;
        List<Map<String, Object>> courseRevenue = new ArrayList<>();

        for (Object[] r : results) {
            Course course = (Course) r[0];
            BigDecimal revenue = (BigDecimal) r[1];
            total = total.add(revenue);

            Map<String, Object> item = new HashMap<>();
            item.put("courseId", course.getId());
            item.put("courseTitle", course.getTitle());
            item.put("revenue", revenue);
            courseRevenue.add(item);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", total);
        response.put("courses", courseRevenue);
        return response;
    }
    
}
