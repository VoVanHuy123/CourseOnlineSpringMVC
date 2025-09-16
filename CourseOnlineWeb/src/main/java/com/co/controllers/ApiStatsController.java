/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.CourseDTO;
import com.co.pojo.Course;
import com.co.services.StatsServices;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ACER
 */
@RestController
@RequestMapping("/api/secure/stats")
@CrossOrigin
public class ApiStatsController {

    @Autowired
    private StatsServices statsServices;

    @GetMapping("/teacher-courses-create")
    public ResponseEntity<List<Map<String, Object>>> getTeacherCourseStatsByMonthYear(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {
        return ResponseEntity.ok(this.statsServices.getTeacherCourseStatsByMonthYear(month, year));
    }

    @GetMapping("/teacher-courses")
    public ResponseEntity<List<Map<String, Object>>> getTeacherStats() {
        return ResponseEntity.ok(this.statsServices.getTeacherCourseStats());
    }

    @GetMapping("/revenue-by-month")
    public ResponseEntity<BigDecimal> getRevenueByMonth(
            @RequestParam(value = "month",required = false) Integer month) {
        return ResponseEntity.ok(this.statsServices.getRevenueByMonth(month));
    }

    @GetMapping("/teacher")
    public ResponseEntity<Map<String, Object>> getTeacherRevenue(
            @RequestParam(value = "teacherId") Integer teacherId,
            @RequestParam(value = "month", required = false) Integer month) {
        return ResponseEntity.ok(this.statsServices.getTeacherCourseRevenue(teacherId, month));
    }
}
