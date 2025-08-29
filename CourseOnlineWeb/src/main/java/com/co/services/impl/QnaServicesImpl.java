/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.co.dtos.QnaDTO;
import com.co.pojo.Lesson;
import com.co.pojo.Qna;
import com.co.pojo.User;
import com.co.repositories.QnaRepository;
import com.co.services.QnaServices;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class QnaServicesImpl implements QnaServices {

    @Autowired
    private QnaRepository qnaRepo;

    @Override
    public List<Qna> getQnas(Map<String, String> params) {
        return this.qnaRepo.getQnas(params);
    }

    @Override
    public QnaDTO getQnaById(int id) {
        Qna q = this.qnaRepo.getQnaById(id);
        if (q == null) return null;

        QnaDTO dto = new QnaDTO();
        dto.setId(q.getId());
        dto.setContent(q.getContent());
        dto.setCreatedAt(q.getCreatedAt());
        dto.setUpdatedAt(q.getUpdatedAt());
        dto.setLessonId(q.getLessonId() != null ? q.getLessonId().getId() : null);
        dto.setUserId(q.getUserId() != null ? q.getUserId().getId() : null);
        dto.setParentId(q.getParentId() != null ? q.getParentId().getId() : null);
        return dto;
    }

    @Override
    public void addOrUpdate(QnaDTO dto) {
        Qna q;
        if (dto.getId() == null) {
            q = new Qna();
            q.setCreatedAt(new Date());
        } else {
            q = qnaRepo.getQnaById(dto.getId());
            if (q == null) throw new IllegalArgumentException("QnA không tồn tại!");
            q.setUpdatedAt(new Date());
        }

        q.setContent(dto.getContent());
        // gán quan hệ
        if (dto.getLessonId() != null) {
            Lesson l = new Lesson();
            l.setId(dto.getLessonId());
            q.setLessonId(l);
        }
        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            q.setUserId(u);
        }
        if (dto.getParentId() != null) {
            Qna parent = new Qna();
            parent.setId(dto.getParentId());
            q.setParentId(parent);
        }

        qnaRepo.addOrUpdate(q);
    }

    @Override
    public void delete(int id) {
        this.qnaRepo.delete(id);
    }
}
