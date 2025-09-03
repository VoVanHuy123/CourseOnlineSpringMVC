/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;


import com.co.pojo.Lesson;
import com.co.repositories.LessonRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ACER
 */
@Repository
@Transactional
public class LessonRepsitoryImpl implements  LessonRepository{
    private static final int PAGE_SIZE = 8;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Lesson> getLessons(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Lesson> query = b.createQuery(Lesson.class);
        Root root = query.from(Lesson.class);
        query.select(root);
        boolean isPagination = true;

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", kw)));
            }

            String chapterId = params.get("chapterId");
            if (chapterId != null && !chapterId.isEmpty()) {
                isPagination = false;
                predicates.add(b.equal(root.get("chapterId").get("id"), Integer.valueOf(chapterId)));
            }

            query.where(predicates);

            // sắp xếp
            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                query.orderBy(b.desc(root.get(orderBy)));
            }
        }

        Query q = s.createQuery(query);

        // phân trang LIMIT OFFSET
        if (isPagination) {
            if (params != null) {
                String p = params.get("page");
                if (p != null && !p.isEmpty()) {
                    int page = Integer.parseInt(p);

                    int start = (page - 1) * PAGE_SIZE;

                    q.setMaxResults(PAGE_SIZE);
                    q.setFirstResult(start);
                }
            }
        }

        return q.getResultList();

    }

    @Override
    public Lesson getLessonById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.find(Lesson.class, id);
    }

    @Override
    public void addOrUpdate(Lesson lesson) {
        Session s = this.factory.getObject().getCurrentSession();

        if (lesson.getId() != null) {
            s.merge(lesson);
        } else {
            s.persist(lesson);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Lesson l = this.getLessonById(id);
        if (l != null) {
            s.remove(l);
        }
    }

    @Override
    public long countLessons(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<Lesson> root = cq.from(Lesson.class);
        cq.select(b.count(root));
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", kw)));
            }

            String chapterId = params.get("chapterId");
            if (chapterId != null && !chapterId.isEmpty()) {
                predicates.add(b.equal(root.get("chapterId").get("id"), Integer.valueOf(chapterId)));
            }

         
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return s.createQuery(cq).getSingleResult();
    }
    
}
