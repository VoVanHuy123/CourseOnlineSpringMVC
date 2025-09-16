/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Lesson;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface LessonRepository {
    public List<Lesson> getLessons(Map<String,String> params);
    public Lesson getLessonById(int id);
    public void addOrUpdate(Lesson chapter);
    public void delete(int id);
    public long countLessons(Map<String, String> params);
    public Long countByCourseIdAndPublic(int courseId, boolean isPublic);
    
}
