/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.co.dtos.ReviewDTO;
import com.co.pojo.Course;
import com.co.pojo.Review;
import com.co.pojo.User;
import com.co.repositories.ReviewRepository;
import com.co.services.ReviewServices;
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
public class ReviewServicesImpl implements ReviewServices{
    @Autowired
    private ReviewRepository reviewRepo;

    @Override
    public List<ReviewDTO> getReviews(Map<String, String> params) {
        return this.reviewRepo.getReviews(params).stream()
                .map(ReviewDTO::new)
                .toList();
    }

    @Override
    public ReviewDTO getReviewById(int id) {
        Review r = this.reviewRepo.getReviewById(id);
        return new ReviewDTO(r);
    }

    @Override
    public void addOrUpdate(ReviewDTO dto) {
        Review r;
        if (dto.getId() != null) {
            r = this.reviewRepo.getReviewById(dto.getId());
            if (r == null) throw new IllegalArgumentException("Review không tồn tại!");
        } else {
            r = new Review();
            r.setCreatedAt(new Date());
        }

        r.setRating(dto.getRating());
        r.setComment(dto.getComment());

        if (dto.getCourseId() != null) {
            Course c = new Course();
            c.setId(dto.getCourseId());
            r.setCourseId(c);
        }

        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            r.setUserId(u);
        }

        this.reviewRepo.addOrUpdate(r);
    }
    @Override
    public ReviewDTO addOrUpdateReview(ReviewDTO dto){
        Review r;
        if (dto.getId() != null) {
            r = this.reviewRepo.getReviewById(dto.getId());
            if (r == null) throw new IllegalArgumentException("Review không tồn tại!");
        } else {
            r = new Review();
            r.setCreatedAt(new Date());
        }

        r.setRating(dto.getRating());
        r.setComment(dto.getComment());

        if (dto.getCourseId() != null) {
            Course c = new Course();
            c.setId(dto.getCourseId());
            r.setCourseId(c);
        }

        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            r.setUserId(u);
        }

//        Review newReview = this.reviewRepo.addOrUpdateReview(r);
        ReviewDTO review = new ReviewDTO(this.reviewRepo.addOrUpdateReview(r));
        return review;
    }
    @Override
    public void delete(int id) {
        this.reviewRepo.delete(id);
    }

    @Override
    public long countReviews(Map<String, String> params) {
        return this.reviewRepo.countReviews(params);
    }
}
