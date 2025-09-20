/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Lesson;
import com.co.pojo.LessonProgress;
import com.co.pojo.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Date;
/**
 *
 * @author ADMIN
 */
public class LessonProgressDTO {
     private Integer id;

    @NotNull(message = "Trạng thái hoàn thành không được để trống")
    private Boolean isCompleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date completedAt;

    @NotNull(message = "Vui lòng chọn bài học")
    @Positive(message = "Bài học không hợp lệ")
    private Integer lessonId;

    private String lessonTitle;

    @NotNull(message = "Vui lòng chọn người dùng")
    @Positive(message = "Người dùng không hợp lệ")
    private Integer userId;

    private String userName;

    public LessonProgressDTO() {}

    public LessonProgressDTO(LessonProgress lp) {
        this.id = lp.getId();
        this.isCompleted = lp.getIsCompleted();
        this.completedAt = lp.getCompletedAt();
        this.lessonId = lp.getLessonId() != null ? lp.getLessonId().getId() : null;
        this.lessonTitle = lp.getLessonId() != null ? lp.getLessonId().getTitle() : null;
        this.userId = lp.getUserId() != null ? lp.getUserId().getId() : null;
        this.userName = lp.getUserId() != null ? lp.getUserId().getFullName() : null;
    }

    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }

    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }

    public Integer getLessonId() { return lessonId; }
    public void setLessonId(Integer lessonId) { this.lessonId = lessonId; }

    public String getLessonTitle() { return lessonTitle; }
    public void setLessonTitle(String lessonTitle) { this.lessonTitle = lessonTitle; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
}
