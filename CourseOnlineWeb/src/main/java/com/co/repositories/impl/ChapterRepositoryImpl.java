/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.pojo.Chapter;
import com.co.repositories.ChapterRepository;
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
public class ChapterRepositoryImpl implements ChapterRepository {

    private static final int PAGE_SIZE = 8;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Chapter> getChapters(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Chapter> query = b.createQuery(Chapter.class);
        Root root = query.from(Chapter.class);
        query.select(root);
        boolean isPagination = true;

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", kw)));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                isPagination = false;
                predicates.add(b.equal(root.get("courseId").get("id"), Integer.valueOf(courseId)));
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
    public Chapter getChapterById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.find(Chapter.class, id);
    }

    @Override
    public void addOrUpdate(Chapter chapter) {
        Session s = this.factory.getObject().getCurrentSession();

        if (chapter.getId() != null) {
            s.merge(chapter);
        } else {
            s.persist(chapter);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Chapter c = this.getChapterById(id);
        if (c != null) {
            s.remove(c);
        }

    }

    @Override
    public long countChapters(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<Chapter> root = cq.from(Chapter.class);
        cq.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            predicates.add(b.like(root.get("title"), "%" + kw + "%"));
        }
        String courseId = params.get("courseId");
        if (courseId != null && !courseId.isEmpty()) {
            predicates.add(b.equal(root.get("courseId").get("id"), Integer.valueOf(courseId)));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return s.createQuery(cq).getSingleResult();
    }

}
