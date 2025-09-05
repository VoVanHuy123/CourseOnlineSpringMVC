/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.co.dtos.ChapterDTO;
import com.co.dtos.CourseDTO;
import com.co.pojo.Course;
import com.co.pojo.User;
import com.co.repositories.CourseRepository;
import com.co.repositories.UserRepository;
import com.co.services.CourseServices;
import com.co.services.UserServices;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACER
 */
@Service
public class CourseServicesImpl implements CourseServices {

    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<CourseDTO> getCourses(Map<String, String> params) {
        List<Course> courses = this.courseRepo.getCourses(params);
        return courses.stream()
                .map(CourseDTO::new) // gọi constructor để convert
                .toList();
    }

    @Override
    public CourseDTO getCourseById(int id ,boolean includeChapters) {
        Course c = this.courseRepo.getCourseById(id,true);
        
        if (c == null) return null;

        CourseDTO courseDto = new CourseDTO();
        courseDto.setId(c.getId());
        courseDto.setTitle(c.getTitle());
        courseDto.setDescription(c.getDescription());
        courseDto.setImageUrl(c.getImageUrl());
        courseDto.setIntroVideoUrl(c.getIntroVideoUrl());
        courseDto.setIsPublic(c.getPublic1());
        courseDto.setTuitionFee(c.getTuitionFee());
        courseDto.setCreatedAt(c.getCreatedAt());
        courseDto.setTeacherId(c.getTeacherId().getId());
        courseDto.setTeacherName(c.getTeacherId().getFullName());
        // map thêm các field cần thiết
       
        if (includeChapters && c.getChapterSet() != null) {
            List<ChapterDTO> chapters = c.getChapterSet().stream()
                    .map(ch -> new ChapterDTO(ch)) // bạn cần constructor trong ChapterDTO
                    .toList();
            courseDto.setChapters(chapters);
        }
        return courseDto;
    }

    @Override
    public void addOrUpdate(CourseDTO dto) {
        Course c;

        if (dto.getId() == null) {
            c = new Course();
            c.setCreatedAt(new Date());
            c.setPublic1(false);
        } else {
            c = courseRepo.getCourseById(dto.getId(),false);
            if (c == null) {
                throw new IllegalArgumentException("Course không tồn tại!");
            }else{
                c.setPublic1(dto.getIsPublic());
                
            }
        }

        // Áp dụng logic validate nâng cao ở đây
        if (dto.getTuitionFee().compareTo(BigDecimal.valueOf(100000000)) > 0) {
            throw new IllegalArgumentException("Học phí không được vượt quá 100 triệu");
        }
        
        User u = this.userRepo.getUserById(dto.getTeacherId());
        if (c == null) {
                throw new IllegalArgumentException("Teacher không tồn tại!");
        }else{
            c.setTeacherId(u);
        }
                
        // Upload ảnh lên Cloudinary (nếu có)
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(dto.getImageFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                c.setImageUrl((String) res.get("secure_url"));
            } catch (IOException ex) {
                Logger.getLogger(CourseServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // Upload video lên Cloudinary (nếu có)
        if (dto.getVideoFile() != null && !dto.getVideoFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(dto.getVideoFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                c.setIntroVideoUrl((String) res.get("secure_url"));
            } catch (IOException ex) {
                Logger.getLogger(CourseServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Map DTO → Entity
        c.setTitle(dto.getTitle());
        c.setDescription(dto.getDescription());
        c.setTuitionFee(dto.getTuitionFee());
        c.setDuration(dto.getDuration());
        c.setLessonsCount(dto.getLessonsCount());
        
        this.courseRepo.addOrUpdate(c);
    }

    @Override
    public void delete(int id) {
        this.courseRepo.delete(id);
    }

    @Override
    public long countCourses(Map<String, String> params) {
        return this.courseRepo.countCourses(params);
    }

}
