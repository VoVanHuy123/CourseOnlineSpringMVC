/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.ChapterDTO;
import com.co.pojo.Chapter;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface ChapterServices {
    public List<ChapterDTO> getChapters(Map<String,String> params);
    public ChapterDTO getChapterById(int id);
    public void addOrUpdate(ChapterDTO chapterDto);
    public void delete(int id);
    public long countChapters(Map<String, String> params);
}
