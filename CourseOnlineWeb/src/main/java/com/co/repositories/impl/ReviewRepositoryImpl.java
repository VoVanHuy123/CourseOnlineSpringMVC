/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.pojo.Review;
import com.co.repositories.ReviewRepository;
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
 * @author ADMIN
 */
@Repository
@Transactional
public class ReviewRepositoryImpl implements ReviewRepository{
     private static final int PAGE_SIZE = 8;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Review> getReviews(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Review> cq = b.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Integer.parseInt(courseId)));
            }
            String userId = params.get("userId");
            if (userId != null && !userId.isEmpty()) {
                predicates.add(b.equal(root.get("userId").get("id"), Integer.parseInt(userId)));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(b.desc(root.get("createdAt")));
        Query<Review> q = s.createQuery(cq);

        // phân trang
        if (params != null) {
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
    public Review getReviewById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Review.class, id);
    }

    @Override
    public void addOrUpdate(Review review) {
        Session s = this.factory.getObject().getCurrentSession();
        if (review.getId() != null) {
            s.merge(review);
        } else {
            s.persist(review);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Review r = this.getReviewById(id);
        if (r != null) {
            s.remove(r);
        }
    }

    @Override
    public long countReviews(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<Review> root = cq.from(Review.class);
        cq.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Integer.parseInt(courseId)));
            }
            String userId = params.get("userId");
            if (userId != null && !userId.isEmpty()) {
                predicates.add(b.equal(root.get("userId").get("id"), Integer.parseInt(userId)));
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return s.createQuery(cq).getSingleResult();
    }
    
    @Override
    public Review addOrUpdateReview(Review review) {
        Session s = this.factory.getObject().getCurrentSession();
        if (review.getId() != null) {
            review = (Review) s.merge(review); // merge trả về entity đã được quản lý
        } else {
            s.persist(review); // persist không trả về, nhưng review sẽ có id sau flush
        }
        return review; // ✅ trả về review với id đã có
    }
}
