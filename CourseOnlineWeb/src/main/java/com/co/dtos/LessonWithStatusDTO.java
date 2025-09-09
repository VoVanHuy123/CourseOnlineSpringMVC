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
public class LessonWithStatusDTO extends LessonDTO{
     private boolean isCompleted;
    private boolean isLocked;
    public LessonWithStatusDTO(Lesson lesson) {
        super(lesson);
        
    }
    /**
     * @return the isCompleted
     */
    public boolean isIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted the isCompleted to set
     */
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return the isLocked
     */
    public boolean isIsLocked() {
        return isLocked;
    }

    /**
     * @param isLocked the isLocked to set
     */
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
   
}
