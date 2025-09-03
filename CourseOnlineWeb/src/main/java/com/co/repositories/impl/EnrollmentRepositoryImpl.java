/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.pojo.Enrollment;
import com.co.repositories.EnrollmentRepository;
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
public class EnrollmentRepositoryImpl implements EnrollmentRepository{
     private static final int PAGE_SIZE = 8;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Enrollment> getEnrollments(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Enrollment> cq = b.createQuery(Enrollment.class);
        Root<Enrollment> root = cq.from(Enrollment.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        boolean isPagination = true;

        if (params != null) {
            // Tìm theo userId
            String userId = params.get("userId");
            if (userId != null && !userId.isEmpty()) {
                predicates.add(b.equal(root.get("userId").get("id"), Integer.valueOf(userId)));
                isPagination = false;
            }

            // Tìm theo courseId
            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Integer.valueOf(courseId)));
                isPagination = false;
            }
            
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                Predicate firstNameLike = b.like(root.get("userId").get("firstName"), String.format("%%%s%%", name));
                Predicate lastNameLike = b.like(root.get("userId").get("lastName"),String.format("%%%s%%", name));
                predicates.add(b.or(firstNameLike, lastNameLike));
            }
            
            String courseTitle = params.get("courseTitle");
            if (courseTitle != null && !courseTitle.isEmpty()) {
                Predicate courseTitleLike = b.like(root.get("courseId").get("title"), String.format("%%%s%%", courseTitle));
                predicates.add(b.or(courseTitleLike));
            }
            
            cq.where(predicates.toArray(new Predicate[0]));

            // Sắp xếp
            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                cq.orderBy(b.desc(root.get(orderBy)));
            }
        }

        Query q = s.createQuery(cq);

        // Phân trang
        if (isPagination && params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                int page = Integer.parseInt(p);
                int start = (page - 1) * PAGE_SIZE;
                q.setMaxResults(PAGE_SIZE);
                q.setFirstResult(start);
            }
        }

        return q.getResultList();
    }

    @Override
    public Enrollment getEnrollmentById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.find(Enrollment.class, id);
    }

    @Override
    public void addOrUpdate(Enrollment e) {
        Session s = this.factory.getObject().getCurrentSession();
        if (e.getId() != null) {
            s.merge(e);
        } else {
            s.persist(e);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Enrollment e = this.getEnrollmentById(id);
        if (e != null) {
            s.remove(e);
        }
    }

    @Override
    public long countEnrollments(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<Enrollment> root = cq.from(Enrollment.class);
        cq.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String userId = params.get("userId");
            if (userId != null && !userId.isEmpty()) {
                predicates.add(b.equal(root.get("userId").get("id"), Integer.valueOf(userId)));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Integer.valueOf(courseId)));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return s.createQuery(cq).getSingleResult();
    }
}
