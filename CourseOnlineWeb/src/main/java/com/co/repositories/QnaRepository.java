/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Qna;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface QnaRepository {
    public List<Qna> getQnas(Map<String, String> params);
    public Qna getQnaById(int id);
    public void addOrUpdate(Qna q);
    public void delete(int id); 
}
