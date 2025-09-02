/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Lesson;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ACER
 */
public class LessonDTO {
    private Integer id;
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;
    private String content;
    private Boolean isPublic;
    private Date createdAt;
    private String videoUrl;
    @NotNull(message = "Thứ tự bài học không được bỏ trống")
    @Min(value = 1, message = "Thứ tự phải >= 1")
    private Integer lessonOrder;

    // chỉ giữ thông tin nhẹ về chapter (tránh vòng lặp vô hạn)
    @NotNull(message = "Id chương không được bỏ trống")
    private Integer chapterId;
    private String chapterTitle;
    
    private MultipartFile videoFile;

    public LessonDTO() {
    }
    public LessonDTO(Lesson lesson){
        this.id=lesson.getId();
        this.title = lesson.getTitle();
        this.content = lesson.getContent();
        this.isPublic = lesson.getPublic1();
        this.createdAt = lesson.getCreatedAt();
        this.videoUrl = lesson.getVideoUrl();
        this.lessonOrder = lesson.getLessonOrder();
        this.chapterId = lesson.getChapterId().getId();
        this.chapterTitle = lesson.getChapterId().getTitle();
    }
    public LessonDTO(Integer id, String title, String content,
                     Boolean isPublic, Date createdAt,
                     Integer chapterId, String chapterTitle) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
    }

    // Getters & Setters
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

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getChapterId() {
        return chapterId;
    }
    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }
    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    /**
     * @return the videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * @param videoUrl the videoUrl to set
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * @return the lessonOrder
     */
    public Integer getLessonOrder() {
        return lessonOrder;
    }

    /**
     * @param lessonOrder the lessonOrder to set
     */
    public void setLessonOrder(Integer lessonOrder) {
        this.lessonOrder = lessonOrder;
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
}
