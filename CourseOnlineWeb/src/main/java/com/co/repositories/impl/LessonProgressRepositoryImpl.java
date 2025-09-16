/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.pojo.Lesson;
import com.co.pojo.LessonProgress;
import com.co.pojo.User;
import com.co.repositories.LessonProgressRepository;
import com.co.repositories.LessonRepository;
import com.co.repositories.UserRepository;
import static jakarta.persistence.Timeout.s;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class LessonProgressRepositoryImpl implements LessonProgressRepository {

    private static final int PAGE_SIZE = 8;

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private UserRepository userRepol;
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<LessonProgress> getLessonProgresses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<LessonProgress> cq = b.createQuery(LessonProgress.class);
        Root<LessonProgress> root = cq.from(LessonProgress.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String userId = params.get("userId");
            if (userId != null && !userId.isEmpty()) {
                predicates.add(b.equal(root.get("userId").get("id"), Integer.parseInt(userId)));
            }
            String lessonId = params.get("lessonId");
            if (lessonId != null && !lessonId.isEmpty()) {
                predicates.add(b.equal(root.get("lessonId").get("id"), Integer.parseInt(lessonId)));
            }
            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("lessonId").get("chapterId").get("courseId").get("id"), Integer.parseInt(courseId)));
            }
            String completed = params.get("isCompleted");
            if (completed != null && !completed.isEmpty()) {
                predicates.add(b.equal(root.get("isCompleted"), Boolean.valueOf(completed)));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        Query q = s.createQuery(cq);

        if (params != null) {
            String p = params.get("page");
            if (p != null) {
                int page = Integer.parseInt(p);
                q.setFirstResult((page - 1) * PAGE_SIZE);
                q.setMaxResults(PAGE_SIZE);
            }
        }

        return q.getResultList();
    }

    @Override
    public LessonProgress getLessonProgressById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.find(LessonProgress.class, id);
    }

    @Override
    public void addOrUpdate(LessonProgress lp) {
        Session s = this.factory.getObject().getCurrentSession();
        if (lp.getId() == null) {
            s.persist(lp);
        } else {
            s.merge(lp);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        LessonProgress lp = getLessonProgressById(id);
        if (lp != null) {
            s.remove(lp);
        }
    }

    @Override
    public long countLessonProgress(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<LessonProgress> root = cq.from(LessonProgress.class);
        cq.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String userId = params.get("userId");
            if (userId != null && !userId.isEmpty()) {
                predicates.add(b.equal(root.get("userId").get("id"), Integer.parseInt(userId)));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public void markComplete(int userId, int lessonId) {
        Session s = this.factory.getObject().getCurrentSession();
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("lessonId", String.valueOf(lessonId));

        List<LessonProgress> results = this.getLessonProgresses(params);
        LessonProgress lp;
        if (!results.isEmpty()) {
            // Đã có -> cập nhật
            lp = results.get(0);
            lp.setIsCompleted(true);
            lp.setCompletedAt(new Date());
            s.merge(lp);
        }else {
            User user = this.userRepol.getUserById(userId);
            Lesson lesson = this.lessonRepository.getLessonById(lessonId);
            lp = new LessonProgress();
            lp.setUserId(user);        // hoặc set User object nếu mapping ManyToOne
            lp.setLessonId(lesson);    // hoặc set Lesson object
            lp.setIsCompleted(true);
            lp.setCompletedAt(new Date());
            s.persist(lp);
        }
    }

    @Override
    public Set<Integer> findCompletedLessonIds(int userId , int courseId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("courseId", String.valueOf(courseId));
        params.put("isCompleted", "true");  // chỉ lấy lesson đã hoàn thành

        List<LessonProgress> results = this.getLessonProgresses(params);

        return results.stream()
                .map(lp -> lp.getLessonId().getId())
                .collect(Collectors.toSet());
    }
}