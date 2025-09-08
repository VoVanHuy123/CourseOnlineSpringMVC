/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.co.dtos.LessonProgressDTO;
import com.co.pojo.Lesson;
import com.co.pojo.LessonProgress;
import com.co.pojo.User;
import com.co.repositories.LessonProgressRepository;
import com.co.repositories.UserRepository;
import com.co.services.LessonProgressServices;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class LessonProgressServicesImpl implements LessonProgressServices{
    @Autowired
    private LessonProgressRepository lpRepo;
    @Autowired
    private UserRepository userRepo;

    @Override
    public List<LessonProgressDTO> getLessonProgresses(Map<String, String> params) {
        return lpRepo.getLessonProgresses(params)
                .stream().map(LessonProgressDTO::new).toList();
    }

    @Override
    public LessonProgressDTO getLessonProgressById(int id) {
        LessonProgress lp = lpRepo.getLessonProgressById(id);
        return lp != null ? new LessonProgressDTO(lp) : null;
    }

    @Override
    public void addOrUpdate(LessonProgressDTO dto) {
        LessonProgress lp = dto.getId() != null ? 
                lpRepo.getLessonProgressById(dto.getId()) : new LessonProgress();

        lp.setIsCompleted(dto.getIsCompleted());
        lp.setCompletedAt(dto.getIsCompleted() ? new Date() : null);

        Lesson lesson = new Lesson();
        lesson.setId(dto.getLessonId());
        lp.setLessonId(lesson);

        User user = userRepo.getUserById(dto.getUserId());
        lp.setUserId(user);

        lpRepo.addOrUpdate(lp);
    }

    @Override
    public void delete(int id) {
        lpRepo.delete(id);
    }

    @Override
    public long countLessonProgress(Map<String, String> params) {
        return lpRepo.countLessonProgress(params);
    }

    @Override
    public void markComplete(int userId, int lessonId) {
        this.lpRepo.markComplete(userId, lessonId);
    }

    @Override
    public Set<Integer> findCompletedLessonIds(int userId) {
        return this.lpRepo.findCompletedLessonIds(userId);
    }
}
