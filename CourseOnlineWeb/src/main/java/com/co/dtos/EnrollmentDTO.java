/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Enrollment;
import java.util.Date;

/**
 *
 * @author ACER
 */
public class EnrollmentDTO {
    private Integer id;
    private Date enrolledAt;

    // Chỉ giữ thông tin cơ bản về course
    private Integer courseId;
    private String courseTitle;

    // Chỉ giữ thông tin cơ bản về user
    private Integer userId;
    private String userFullName; // hoặc email

    // Thông tin về payments (nếu muốn)
//    private Set<PaymentDTO> payments;

    /**
     * @return the id
     */
    public EnrollmentDTO(){
        
    }
    public EnrollmentDTO(Enrollment e){
        this.id = e.getId();
        this.enrolledAt = e.getEnrolledAt();
        this.courseId = e.getCourseId().getId();
        this.courseTitle = e.getCourseId().getTitle();
        this.userId = e.getUserId().getId();
        this.userFullName = e.getUserId().getFullName();
    }
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the enrolledAt
     */
    public Date getEnrolledAt() {
        return enrolledAt;
    }

    /**
     * @param enrolledAt the enrolledAt to set
     */
    public void setEnrolledAt(Date enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    /**
     * @return the courseId
     */
    public Integer getCourseId() {
        return courseId;
    }

    /**
     * @param courseId the courseId to set
     */
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the userFullName
     */
    public String getUserFullName() {
        return userFullName;
    }

    /**
     * @param userFullName the userFullName to set
     */
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
