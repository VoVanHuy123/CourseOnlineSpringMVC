/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Lesson;

/**
 *
 * @author ACER
 */
public class LessonNameDTO {
    private Integer id;
    private String title;
    private Integer chapterId;
    public LessonNameDTO(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.chapterId = lesson.getChapterId().getId();
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
     * @return the chapterId
     */
    public Integer getChapterId() {
        return chapterId;
    }

    /**
     * @param chapterId the chapterId to set
     */
    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }
}
