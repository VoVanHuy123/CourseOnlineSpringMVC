/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Review;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ReviewRepository {
    public List<Review> getReviews(Map<String, String> params);
    public Review getReviewById(int id);
    public void addOrUpdate(Review review);
    public void delete(int id);
    public long countReviews(Map<String, String> params);   
    public Review addOrUpdateReview(Review review);
}
