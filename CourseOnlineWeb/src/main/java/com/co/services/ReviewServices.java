/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.ReviewDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ReviewServices {
    public List<ReviewDTO> getReviews(Map<String, String> params);
    public ReviewDTO getReviewById(int id);
    public void addOrUpdate(ReviewDTO review);
    public void delete(int id);
    public long countReviews(Map<String, String> params);
    public ReviewDTO addOrUpdateReview(ReviewDTO review);
    
}
