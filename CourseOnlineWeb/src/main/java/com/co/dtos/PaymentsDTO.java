/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.dtos;

import com.co.pojo.Payments;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;
/**
 *
 * @author ADMIN
 */

public class PaymentsDTO {
    private Integer id;
    private BigDecimal amount;
    private String method;
    private String transactionCode;
    private String status;
    private Date paidAt;
    private Integer enrollmentId;

    

    public PaymentsDTO() {}

    public PaymentsDTO(Payments p) {
        this.id = p.getId();
        this.amount = p.getAmount();
        this.method = p.getMethod();
        this.transactionCode = p.getTransactionCode();
        this.status = p.getStatus();
        this.paidAt = p.getPaidAt();
        this.enrollmentId = p.getEnrollmentId() != null ? p.getEnrollmentId().getId() : null;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getTransactionCode() { return transactionCode; }
    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getPaidAt() { return paidAt; }
    public void setPaidAt(Date paidAt) { this.paidAt = paidAt; }

    public Integer getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Integer enrollmentId) { this.enrollmentId = enrollmentId; }
        
}
