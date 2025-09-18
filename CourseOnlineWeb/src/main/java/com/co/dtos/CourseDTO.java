/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ACER
 */
public class CourseDTO {

    /**
     * @return the isPublic
     */
    public Boolean getIsPublic() {
        return isPublic;
    }

    /**
     * @param isPublic the isPublic to set
     */
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    private Integer id;
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

//    @NotNull(message = "Học phí không được bỏ trống")
//    @DecimalMin(value = "0.0", message = "Học phí phải >= 0")
    private BigDecimal tuitionFee = BigDecimal.ZERO;
    
    private String imageUrl;
    private String introVideoUrl;
    
    private BigDecimal duration;
    private Integer lessonsCount;
    
    private Date createdAt = new Date();
    private Boolean isPublic;
    
    private String teacherName;
    private int teacherId;
    
    private String teacherNumber;
    private String teacherEmail;
    private String teacherAvatar;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile imageFile;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile videoFile;
    private List<ChapterDTO> chapters;
    public CourseDTO() {
    }
    public CourseDTO(Course course){
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.tuitionFee = course.getTuitionFee();
        this.imageUrl = course.getImageUrl();
        this.introVideoUrl = course.getIntroVideoUrl();
        this.createdAt = course.getCreatedAt();
        this.isPublic = course.getPublic1();
        this.teacherId = course.getTeacherId().getId();
        this.teacherName = course.getTeacherId().getFullName();
        this.teacherAvatar=course.getTeacherId().getAvatar();
        this.teacherNumber=course.getTeacherId().getPhoneNumber();
        this.teacherEmail = course.getTeacherId().getEmail();
    }
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

    /**
     * @return the chapters
     */
    public List<ChapterDTO> getChapters() {
        return chapters;
    }

    /**
     * @param chapters the chapters to set
     */
    public void setChapters(List<ChapterDTO> chapters) {
        this.chapters = chapters;
    }

    /**
     * @return the teacherName
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName the teacherName to set
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * @return the teacherId
     */
    public int getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return the teacherNumber
     */
    public String getTeacherNumber() {
        return teacherNumber;
    }

    /**
     * @param teacherNumber the teacherNumber to set
     */
    public void setTeacherNumber(String teacherNumber) {
        this.teacherNumber = teacherNumber;
    }

    /**
     * @return the teacherEmail
     */
    public String getTeacherEmail() {
        return teacherEmail;
    }

    /**
     * @param teacherEmail the teacherEmail to set
     */
    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    /**
     * @return the teacherAvatar
     */
    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    /**
     * @param teacherAvatar the teacherAvatar to set
     */
    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    
    
}
