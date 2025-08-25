/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

//import com.dht.services.CategoryServices;
//import com.dht.services.ProductServices;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
@ControllerAdvice
public class IndexController {
//    @Autowired
//    private CategoryServices cateService;
//    @Autowired
//    private ProductServices prodService;
    
//    @ModelAttribute
//    public void commonResponse(Model model) {
//        model.addAttribute("categories", this.cateService.getCates());
//    } 
//    
    @RequestMapping("/")
    public String index(Model model, @RequestParam Map<String, String> params) {
        
//        model.addAttribute("products", this.prodService.getProducts(params));
        return "index";
    }
}
