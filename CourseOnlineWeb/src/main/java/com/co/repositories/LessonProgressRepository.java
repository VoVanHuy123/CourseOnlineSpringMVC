/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.LessonProgress;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
public interface LessonProgressRepository {
    public List<LessonProgress> getLessonProgresses(Map<String, String> params);
    public LessonProgress getLessonProgressById(int id);
    public void addOrUpdate(LessonProgress lp);
    public void delete(int id);
    public long countLessonProgress(Map<String, String> params);
    public void markComplete(int userId, int lessonId);
    public Set<Integer> findCompletedLessonIds(int userId,int courseId);
}
