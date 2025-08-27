/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ACER
 */
public class CourseDTO {
    private Integer id;
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Học phí không được bỏ trống")
    @DecimalMin(value = "0.0", message = "Học phí phải >= 0")
    private BigDecimal tuitionFee;
    
    private String imageUrl;
    private String introVideoUrl;
    
    private BigDecimal duration;
    private Integer lessonsCount;
    
    private Date createdAt = new Date();
    private Boolean public1;

    private MultipartFile imageFile;
    private MultipartFile videoFile;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the tuitionFee
     */
    public BigDecimal getTuitionFee() {
        return tuitionFee;
    }

    /**
     * @param tuitionFee the tuitionFee to set
     */
    public void setTuitionFee(BigDecimal tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    /**
     * @return the duration
     */
    public BigDecimal getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    /**
     * @return the lessonsCount
     */
    public Integer getLessonsCount() {
        return lessonsCount;
    }

    /**
     * @param lessonsCount the lessonsCount to set
     */
    public void setLessonsCount(Integer lessonsCount) {
        this.lessonsCount = lessonsCount;
    }

    /**
     * @return the public1
     */
    public Boolean getPublic1() {
        return public1;
    }

    /**
     * @param public1 the public1 to set
     */
    public void setPublic1(Boolean public1) {
        this.public1 = public1;
    }

    /**
     * @return the imageFile
     */
    public MultipartFile getImageFile() {
        return imageFile;
    }

    /**
     * @param imageFile the imageFile to set
     */
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * @return the videoFile
     */
    public MultipartFile getVideoFile() {
        return videoFile;
    }

    /**
     * @param videoFile the videoFile to set
     */
    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }

    /**
     * @return the id
     */
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
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return the introVideoUrl
     */
    public String getIntroVideoUrl() {
        return introVideoUrl;
    }

    /**
     * @param introVideoUrl the introVideoUrl to set
     */
    public void setIntroVideoUrl(String introVideoUrl) {
        this.introVideoUrl = introVideoUrl;
    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    
    
}
