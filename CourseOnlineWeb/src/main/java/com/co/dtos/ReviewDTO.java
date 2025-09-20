/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class ReviewDTO {
    private Integer id;
    @NotNull(message = "Đánh giá không được để trống")
    @Min(value = 1, message = "Đánh giá phải từ 1 đến 5 sao")
    @Max(value = 5, message = "Đánh giá phải từ 1 đến 5 sao")
    private Integer rating;

    @NotBlank(message = "Bình luận không được để trống")
    private String comment;

    private Date createdAt;

    @NotNull(message = "Khoá học không được để trống")
    private Integer courseId;

    private String courseTitle;

    @NotNull(message = "Người dùng không được để trống")
    private Integer userId;
    private String userFullName;
    private String userAvatar;

    public ReviewDTO() {}

   
    public ReviewDTO(Review r) {
        this.id = r.getId();
        this.rating = r.getRating();
        this.comment = r.getComment();
        this.createdAt = r.getCreatedAt();
        if (r.getCourseId() != null) {
            this.courseId = r.getCourseId().getId();
            this.courseTitle = r.getCourseId().getTitle();
        }
        if (r.getUserId() != null) {
            this.userId = r.getUserId().getId();
            this.userFullName = r.getUserId().getFullName();
            this.userAvatar = r.getUserId().getAvatar();
        }
    }

    // getter - setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    /**
     * @return the userAvatar
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * @param userAvatar the userAvatar to set
     */
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
