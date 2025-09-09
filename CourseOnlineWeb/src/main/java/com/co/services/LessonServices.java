/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.LessonDTO;
import com.co.dtos.LessonWithStatusDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface LessonServices {
    public List<LessonDTO> getLessons(Map<String,String> params);
    public LessonDTO getLessonById(int id);
    public void addOrUpdate(LessonDTO chapterDto);
    public void delete(int id);
    public long countLessons(Map<String, String> params);
    public List<LessonWithStatusDTO> getLessonsWithStatus(Integer courseId, Integer userId);
}
