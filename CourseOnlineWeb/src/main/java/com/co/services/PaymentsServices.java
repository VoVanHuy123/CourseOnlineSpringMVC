/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.services;

import com.co.dtos.PaymentsDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface PaymentsServices {
    public List<PaymentsDTO> getPayments(Map<String, String> params);
    public PaymentsDTO getPaymentById(int id);
    public void addOrUpdate(PaymentsDTO payment);
    public void delete(int id);
    public long countPayments(Map<String, String> params);    
}
