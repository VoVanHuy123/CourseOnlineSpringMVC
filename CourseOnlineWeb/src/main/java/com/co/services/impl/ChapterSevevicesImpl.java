/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.co.dtos.ChapterDTO;
import com.co.dtos.LessonNameDTO;
import com.co.pojo.Chapter;
import com.co.pojo.Course;
import com.co.repositories.ChapterRepository;
import com.co.repositories.CourseRepository;
import com.co.services.ChapterServices;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 *
 * @author ACER
 */
@Service
public class ChapterSevevicesImpl implements ChapterServices {

    @Autowired
    private ChapterRepository chapterRepo;
    @Autowired
    private CourseRepository courseRepo;

//    @Override
    public List<ChapterDTO> getChapters(Map<String, String> params) {
//        List<Chapter> chapters = this.chapterRepo.getChapters(params);
//        return chapters.stream()
//                .map(ChapterDTO::new) // gọi constructor để convert
//                .toList();
        List<Chapter> chapters = this.chapterRepo.getChapters(params);
        List<Integer> chapterIds = chapters.stream().map(Chapter::getId).toList();
        Map<Integer, List<LessonNameDTO>> lessonMap = this.chapterRepo.getLessonsForChapters(chapterIds);
        List<ChapterDTO> dtoList = chapters.stream().map(c -> {
            ChapterDTO dto = new ChapterDTO(c);
                dto.setLessons(lessonMap.getOrDefault(c.getId(), Collections.emptyList()));
                return dto;
            }).toList();
        return dtoList;
    }

    @Override
    public ChapterDTO getChapterById(int id) {
        Chapter c = this.chapterRepo.getChapterById(id);
        if (c != null) {
            ChapterDTO cdto = this.convertToDTO(c);
            return cdto;

        }
        throw new IllegalArgumentException("Course không tồn tại!");
    }

    @Override
    public void addOrUpdate(ChapterDTO chapterDto) {
        Chapter c;

        if (chapterDto.getId() == null) {
            c = new Chapter();
            c.setCreatedAt(new Date());
        } else {
            c = chapterRepo.getChapterById(chapterDto.getId());
            if (c == null) {
                throw new IllegalArgumentException("Course không tồn tại!");
            }
        }
        if (chapterDto.getCourseId() != null) {
            Course course = courseRepo.getCourseById(chapterDto.getCourseId(), false);
            if (course == null) {
                throw new RuntimeException("không tìm thấy Course");
            }
            c.setCourseId(course);
        }
        c.setTitle(chapterDto.getTitle());
        c.setDescription(chapterDto.getDescription());
        c.setOrderIndex(chapterDto.getOrderIndex());
        c.setUpdatedAt(new Date());
        this.chapterRepo.addOrUpdate(c);
    }

    @Override
    public void delete(int id) {
        this.chapterRepo.delete(id);
    }

    // Convert Entity → DTO
    private ChapterDTO convertToDTO(Chapter chapter) {
        ChapterDTO dto = new ChapterDTO();
        dto.setId(chapter.getId());
        dto.setTitle(chapter.getTitle());
        dto.setDescription(chapter.getDescription());
        dto.setCourseId(chapter.getCourseId().getId());
        dto.setOrderIndex(chapter.getOrderIndex());
        dto.setCreatedAt(chapter.getCreatedAt());
        dto.setUpdatedAt(chapter.getUpdatedAt());
        return dto;
    }

    @Override
    public long countChapters(Map<String, String> params) {
        return this.chapterRepo.countChapters(params);
    }

}
