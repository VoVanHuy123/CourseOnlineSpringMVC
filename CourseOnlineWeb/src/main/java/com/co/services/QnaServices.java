/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.QnaDTO;
import com.co.pojo.Qna;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface QnaServices {
    public List<Qna> getQnas(Map<String, String> params);
    public QnaDTO getQnaById(int id);
    public void addOrUpdate(QnaDTO dto);
    public void delete(int id); 
}
