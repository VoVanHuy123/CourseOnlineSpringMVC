/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Chapter;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface ChapterRepository {
    public List<Chapter> getChapters(Map<String,String> params);
    public Chapter getChapterById(int id);
    public void addOrUpdate(Chapter chapter);
    public void delete(int id);
    public long countChapters(Map<String, String> params);
}
