/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Chapter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ACER
 */
public class ChapterDTO {
    private Integer id;
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;
    private String description;
    @NotNull(message = "Thứ tự chương không được để trống")
    private Integer orderIndex;
    private Date createdAt;
    private Date updatedAt;

    // chỉ lấy id hoặc title của course thay vì cả object
    private Integer courseId;
    private String courseTitle;

    // nếu cần trả về danh sách lessons
    
    private List<LessonDTO> lessons;

    public ChapterDTO() {}
    
    public ChapterDTO(Chapter chapter) {
        this.id = chapter.getId();
        this.title = chapter.getTitle();
        this.description = chapter.getDescription();
        this.orderIndex = chapter.getOrderIndex();
        this.createdAt = chapter.getCreatedAt();
        this.updatedAt = chapter.getUpdatedAt();
        if (chapter.getCourseId() != null) {
            this.courseId = chapter.getCourseId().getId();
        }
    }

    public ChapterDTO(Integer id, String title, String description,
                      Integer orderIndex, Date createdAt, Date updatedAt,
                      Integer courseId, String courseTitle) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    // getters & setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCourseId() {
        return courseId;
    }
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public List<LessonDTO> getLessons() {
        return lessons;
    }
    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons;
    }
}
