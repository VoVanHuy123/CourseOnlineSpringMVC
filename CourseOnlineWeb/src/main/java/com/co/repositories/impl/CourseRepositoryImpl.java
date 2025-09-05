/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.pojo.Course;
import com.co.repositories.CourseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
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
public class CourseRepositoryImpl implements CourseRepository {

    private static final int PAGE_SIZE = 8;
    private static final int DEFAULT_PAGE = 1;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Course> query = b.createQuery(Course.class);
        Root root = query.from(Course.class);
        query.select(root);

        //lọc
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", kw)));
            }
            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("tuitionFee"), new BigDecimal(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("tuitionFee"), new BigDecimal(toPrice)));
            }

            String teacherId = params.get("teacherId");
            if (teacherId != null && !teacherId.isEmpty()) {
                predicates.add(b.equal(root.get("teacherId").get("id"), Integer.valueOf(teacherId)));
            }
            
            String isPublic = params.get("isPublic");
            if (isPublic != null && !isPublic.isEmpty()) {
                predicates.add(b.equal(root.get("public1"), Boolean.valueOf(isPublic)));
            }

            query.where(predicates);

            //sắp xếp
            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                query.orderBy(b.desc(root.get(orderBy)));
            }
        }

        Query q = s.createQuery(query);
        // phân trang LIMIT OFFSET
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                int page = Integer.parseInt(p);

                int start = (page - 1) * PAGE_SIZE;

                q.setMaxResults(PAGE_SIZE);
                q.setFirstResult(start);
            } else {
                int page = DEFAULT_PAGE;

                int start = (page - 1) * PAGE_SIZE;

                q.setMaxResults(PAGE_SIZE);
                q.setFirstResult(start);
            }
        }

        return q.getResultList();
    }

    @Override
    public Course getCourseById(int id, boolean includeChapters) {
        Session s = this.factory.getObject().getCurrentSession();
//        return s.find(Course.class, id);
        if (includeChapters) {
            // load luôn chapters
            return s.createQuery(
                    "SELECT c FROM Course c LEFT JOIN FETCH c.chapterSet WHERE c.id = :id",
                    Course.class
            )
                    .setParameter("id", id)
                    .uniqueResult();
        } else {
            // chỉ lấy course, không chapters
//            return s.createQuery(
//                    "SELECT c FROM Course c WHERE c.id = :id",
//                    Course.class
//                )
//                .setParameter("id", id)
//                .uniqueResult();
            return s.find(Course.class, id);
        }
    }

    @Override
    public void addOrUpdate(Course c) {
        Session s = this.factory.getObject().getCurrentSession();

        if (c.getId() != null) {
            s.merge(c);
        } else {
            s.persist(c);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Course c = this.getCourseById(id,false);
        if (c != null) {
            s.remove(c);
        }

    }

    @Override
    public long countCourses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<Course> root = cq.from(Course.class);
        cq.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            predicates.add(b.like(root.get("title"), String.format("%%%s%%", kw)));
        }
        String fromPrice = params.get("fromPrice");
        if (fromPrice != null && !fromPrice.isEmpty()) {
            predicates.add(b.greaterThanOrEqualTo(root.get("tuitionFee"), new BigDecimal(fromPrice)));
        }

        String toPrice = params.get("toPrice");
        if (toPrice != null && !toPrice.isEmpty()) {
            predicates.add(b.lessThanOrEqualTo(root.get("tuitionFee"), new BigDecimal(toPrice)));
        }

        String teacherId = params.get("teacherId");
        if (teacherId != null && !teacherId.isEmpty()) {
            predicates.add(b.equal(root.get("teacherId").get("id"), Integer.valueOf(teacherId)));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return s.createQuery(cq).getSingleResult();
    }

}
