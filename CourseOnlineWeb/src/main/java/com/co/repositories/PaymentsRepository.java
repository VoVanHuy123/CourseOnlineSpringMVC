/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.co.repositories;

import com.co.pojo.Payments;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface PaymentsRepository {
    public List<Payments> getPayments(Map<String, String> params);
    public Payments getPaymentById(int id);
    public void addOrUpdate(Payments payment);
    public void delete(int id);
    public long countPayments(Map<String, String> params);
}

