/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.co.dtos.LessonDTO;
import com.co.dtos.LessonWithStatusDTO;
import com.co.pojo.Chapter;
import com.co.pojo.Course;
import com.co.pojo.Lesson;
import com.co.repositories.ChapterRepository;
import com.co.repositories.CourseRepository;
import com.co.repositories.LessonRepository;
import com.co.services.LessonProgressServices;
import com.co.services.LessonServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACER
 */
@Service
public class LessonServicesImpl implements LessonServices {

    @Autowired
    private LessonRepository lessonRepo;
    @Autowired
    private ChapterRepository chapterRepo;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonProgressServices lpServices;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<LessonDTO> getLessons(Map<String, String> params) {
        List<Lesson> lessons = this.lessonRepo.getLessons(params);
        return lessons.stream()
                .map(LessonDTO::new) // gọi constructor để convert
                .toList();
    }

    @Override
    public LessonDTO getLessonById(int id) {
        Lesson c = this.lessonRepo.getLessonById(id);
        if (c != null) {
            LessonDTO cdto = this.convertToDTO(c);
            return cdto;

        }
        throw new IllegalArgumentException("Course không tồn tại!");
    }

    @Override
    public void addOrUpdate(LessonDTO lessonDTO) {
        Lesson l;

        if (lessonDTO.getId() == null) {
            l = new Lesson();
            l.setCreatedAt(new Date());
        } else {
            l = lessonRepo.getLessonById(lessonDTO.getId());
            if (l == null) {
                throw new IllegalArgumentException("Course không tồn tại!");
            }
        }
        if (lessonDTO.getChapterId() != null) {
            Chapter chapter = this.chapterRepo.getChapterById(lessonDTO.getChapterId());
            if (chapter == null) {
                throw new RuntimeException("không tìm thấy chapter");
            }
            l.setChapterId(chapter);

        }

        // Upload ảnh lên Cloudinary (nếu có)
        if (lessonDTO.getVideoFile() != null && !lessonDTO.getVideoFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(lessonDTO.getVideoFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                l.setVideoUrl((String) res.get("secure_url"));
            } catch (IOException ex) {
                Logger.getLogger(CourseServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        l.setTitle(lessonDTO.getTitle());
        l.setContent(lessonDTO.getContent());
        l.setLessonOrder(lessonDTO.getLessonOrder());
        l.setPublic1(lessonDTO.getIsPublic());

        this.lessonRepo.addOrUpdate(l);

        // --- Cập nhật lại lessonsCount cho Course ---
        Course course = this.courseRepository.getCourseById(
                l.getChapterId().getCourseId().getId(), false
        );

        if (course != null) {
            // Đếm số lesson public = true trong course này
            Long publicLessonCount = this.lessonRepo.countByCourseIdAndPublic(
                    course.getId(), true
            );

            course.setLessonsCount(publicLessonCount.intValue());
            this.courseRepository.addOrUpdate(course);
        }

    }

    @Override
    public void delete(int id) {
        this.lessonRepo.delete(id);
    }

    @Override
    public long countLessons(Map<String, String> params) {
        return this.lessonRepo.countLessons(params);
    }

    // Convert Entity → DTO
    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setChapterId(lesson.getChapterId().getId());
        dto.setChapterTitle(lesson.getChapterId().getTitle());
        dto.setLessonOrder(lesson.getLessonOrder());
        dto.setCreatedAt(lesson.getCreatedAt());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setIsPublic(lesson.getPublic1());
        return dto;

    }

    @Override
    public List<LessonWithStatusDTO> getLessonsWithStatus(Integer courseId, Integer userId) {
        Map<String, String> params = new HashMap<>();
        params.put("courseId", String.valueOf(courseId));
        params.put("isPublic", String.valueOf(true));

        // Lấy tất cả lessons của course
        List<Lesson> lessons = this.lessonRepo.getLessons(params);

        // Sort: chapter.orderIndex -> lessonOrder
        lessons.sort(Comparator
                .comparing((Lesson l) -> l.getChapterId().getOrderIndex()) // hoặc getChapter().getOrderIndex()
                .thenComparing(Lesson::getLessonOrder));

        // Lấy set các lessonId đã hoàn thành
        Set<Integer> completedLessonIds = this.lpServices.findCompletedLessonIds(userId,courseId);

        List<LessonWithStatusDTO> result = new ArrayList<>();

        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            LessonWithStatusDTO dto = new LessonWithStatusDTO(lesson);

            boolean isCompleted = completedLessonIds.contains(lesson.getId());
            dto.setIsCompleted(isCompleted);

            if (i == 0) {
                // Bài đầu tiên của toàn khóa luôn mở
                dto.setIsLocked(false);
            } else {
                // Bài hiện tại mở khi bài trước (trong danh sách đã sort) đã hoàn thành
                Lesson prevLesson = lessons.get(i - 1);
                boolean prevCompleted = completedLessonIds.contains(prevLesson.getId());
                dto.setIsLocked(!prevCompleted);
            }

            result.add(dto);
        }

        return result;
    }

}
