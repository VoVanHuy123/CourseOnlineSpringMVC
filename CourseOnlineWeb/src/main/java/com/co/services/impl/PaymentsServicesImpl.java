/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.services.impl;

import com.co.dtos.PaymentsDTO;
import com.co.pojo.Enrollment;
import com.co.pojo.Payments;
import com.co.repositories.PaymentsRepository;
import com.co.services.PaymentsServices;
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
public class PaymentsServicesImpl implements  PaymentsServices{
    
    @Autowired
    private PaymentsRepository paymentsRepo;

    @Override
    public List<PaymentsDTO> getPayments(Map<String, String> params) {
        return paymentsRepo.getPayments(params)
                .stream()
                .map(PaymentsDTO::new)
                .toList();
    }

    @Override
    public PaymentsDTO getPaymentById(int id) {
        return new PaymentsDTO(paymentsRepo.getPaymentById(id));
    }

    @Override
    public void addOrUpdate(PaymentsDTO dto) {
        Payments p;
        if (dto.getId() != null) {
            p = paymentsRepo.getPaymentById(dto.getId());
        } else {
            p = new Payments();
            p.setPaidAt(new Date());
        }
        p.setAmount(dto.getAmount());
        p.setMethod(dto.getMethod());
        p.setTransactionCode(dto.getTransactionCode());
        p.setStatus(dto.getStatus());
        if (dto.getEnrollmentId() != null) {
            Enrollment e = new Enrollment();
            e.setId(dto.getEnrollmentId());
            p.setEnrollmentId(e);
        }
        paymentsRepo.addOrUpdate(p);
    }

    @Override
    public void delete(int id) {
        paymentsRepo.delete(id);
    }

    @Override
    public long countPayments(Map<String, String> params) {
        return paymentsRepo.countPayments(params);
    }
}

