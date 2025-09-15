/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.controllers;

import com.co.dtos.PaymentsDTO;
import com.co.dtos.EnrollmentDTO;
import com.co.pojo.Enrollment;
import com.co.services.PaymentsServices;
import com.co.services.EnrollmentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiPaymentController {
    private final String vnp_TmnCode = "L3K1P92Z";
    private final String vnp_HashSecret = "TFCTA1RVON3PJUPWF6U6E6PMNE1SMWD3";
    private final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "https://cacb544b215d.ngrok-free.app/CourseOnline/api/payments/vnpay/return";
    //Số thẻ	9704198526191432198
    //Tên chủ thẻ	NGUYEN VAN A
    //Ngày phát hành	07/15
    //Mật khẩu OTP	123456
    @Autowired
    private PaymentsServices paymentsServices;
    
    @Autowired
    private EnrollmentServices enrollmentServices;
    
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] hashBytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC SHA512", e);
        }
    }

    
    /**
     * Lấy danh sách payments - GET /api/payments
     */
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentsDTO>> getPayments(@RequestParam Map<String, String> params) {
        try {
            List<PaymentsDTO> payments = paymentsServices.getPayments(params);
            return ResponseEntity.ok(payments);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * Lấy chi tiết payment theo ID - GET /api/payments/{id}
     */
    @GetMapping("/payments/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable("id") int id) {
        try {
            PaymentsDTO payment = paymentsServices.getPaymentById(id);
            if (payment != null && payment.getId() != null) {
                return ResponseEntity.ok(payment);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Payment not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi server", "message", ex.getMessage()));
        }
    }
    @PostMapping("/secure/enrollments")
    public ResponseEntity<?> createEnrollment(@RequestBody Map<String, Object> request) {
        try {
            Object courseIdObj = request.get("courseId");
            Object userIdObj = request.get("userId");

            if (courseIdObj == null || userIdObj == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "courseId và userId không được để trống"));
            }

            Integer courseId = Integer.valueOf(courseIdObj.toString());
            Integer userId = Integer.valueOf(userIdObj.toString());

            // Kiểm tra tồn tại
            List<EnrollmentDTO> existing = enrollmentServices.getEnrollments(Map.of(
                    "courseId", courseId.toString(),
                    "userId", userId.toString()
            ));
            if (!existing.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "id", existing.get(0).getId(),
                        "status", existing.get(0).getStatus(),
                        "message", "Enrollment already exists"
                ));
            }

            // Tạo mới
            EnrollmentDTO newE = new EnrollmentDTO();
            newE.setCourseId(courseId);
            newE.setUserId(userId);
            newE.setEnrolledAt(new Date());
            newE.setStatus("pending");

            enrollmentServices.addOrUpdate(newE);

            // Lấy lại enrollment
            List<EnrollmentDTO> created = enrollmentServices.getEnrollments(Map.of(
                    "courseId", courseId.toString(),
                    "userId", userId.toString()
            ));
            EnrollmentDTO saved = created.get(0);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "id", saved.getId(),
                    "courseId", saved.getCourseId(),
                    "userId", saved.getUserId(),
                    "status", saved.getStatus(),
                    "message", "Enrollment created successfully"
            ));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi tạo enrollment", "message", ex.getMessage()));
        }
    }

    
    /**
     * Tạo payment mới - POST /api/secure/payments
     * Endpoint này khớp với frontend: 'create_payment': '/secure/payments'
     */
    @PostMapping("/secure/payments")
    public ResponseEntity<?> createPayment(@RequestBody PaymentsDTO dto) {
        try {
            if (dto.getEnrollmentId() == null)
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu enrollmentId"));

            if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
                return ResponseEntity.badRequest().body(Map.of("error", "Số tiền không hợp lệ"));

            if (dto.getMethod() == null || dto.getMethod().isBlank())
                return ResponseEntity.badRequest().body(Map.of("error", "Phương thức không hợp lệ"));

            // Gán default
            dto.setPaidAt(new Date());
            dto.setStatus("pending");
            if (dto.getTransactionCode() == null || dto.getTransactionCode().isBlank()) {
                dto.setTransactionCode("TXN_" + System.currentTimeMillis() + "_" +
                        UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            }

            paymentsServices.addOrUpdate(dto);

            // Nếu VNPay
            if ("vnpay".equalsIgnoreCase(dto.getMethod().trim())) {
                long amount = dto.getAmount().multiply(BigDecimal.valueOf(100)).longValue();
                String vnp_TxnRef = dto.getTransactionCode();

                Map<String, String> vnp_Params = new HashMap<>();
                vnp_Params.put("vnp_Version", "2.1.0");
                vnp_Params.put("vnp_Command", "pay");
                vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
                vnp_Params.put("vnp_Amount", String.valueOf(amount));
                vnp_Params.put("vnp_CurrCode", "VND");
                vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
                vnp_Params.put("vnp_OrderInfo", "Thanh toan enrollmentId=" + dto.getEnrollmentId());
                vnp_Params.put("vnp_OrderType", "other");
                vnp_Params.put("vnp_Locale", "vn");
                // ✅ Gắn enrollmentId và courseId để Return xử lý
                vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
                vnp_Params.put("vnp_IpAddr", "127.0.0.1");
                vnp_Params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

                // sort
                List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
                Collections.sort(fieldNames);

                StringBuilder hashData = new StringBuilder();
                StringBuilder query = new StringBuilder();
                for (String fieldName : fieldNames) {
                    String fieldValue = vnp_Params.get(fieldName);
                    if (fieldValue != null && !fieldValue.isEmpty()) {
                        String encodedKey = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                        String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());
                        
                        hashData.append(encodedKey).append('=').append(encodedValue).append('&');
                        query.append(encodedKey).append('=').append(encodedValue).append('&');
                    }
                }
                hashData.setLength(hashData.length() - 1);
                query.setLength(query.length() - 1);

                String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
                query.append("&vnp_SecureHash=").append(vnp_SecureHash);


                String redirectUrl = vnp_PayUrl + "?" + query;

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("success", true,
                                "redirectUrl", redirectUrl,
                                "transactionCode", dto.getTransactionCode()));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "message", "Payment created"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi tạo payment", "message", ex.getMessage()));
        }
    }

    
    /**
     * Cập nhật payment - PUT /api/secure/payments/{id}
     */
    @PutMapping("/secure/payments/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable("id") int id, @RequestBody PaymentsDTO dto) {
        try {
            // Kiểm tra payment có tồn tại không
            PaymentsDTO existingPayment = paymentsServices.getPaymentById(id);
            if (existingPayment == null || existingPayment.getId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy payment"));
            }
            
            // Set ID cho DTO để update
            dto.setId(id);
            
            // Validation
            if (dto.getAmount() != null && dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Số tiền không hợp lệ"));
            }
            
            paymentsServices.addOrUpdate(dto);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Payment updated successfully"
            ));
            
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi server khi cập nhật thanh toán", "message", ex.getMessage()));
        }
    }
    
    /**
     * Xóa payment - DELETE /api/secure/payments/{id}
     */
    @DeleteMapping("/secure/payments/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable("id") int id) {
        try {
            // Kiểm tra payment có tồn tại không
            PaymentsDTO existingPayment = paymentsServices.getPaymentById(id);
            if (existingPayment == null || existingPayment.getId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy payment"));
            }
            
            paymentsServices.delete(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Payment deleted successfully"
            ));
            
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi server khi xóa thanh toán", "message", ex.getMessage()));
        }
    }
    
    /**
     * Đếm số lượng payments - GET /api/payments/count
     */
    @GetMapping("/payments/count")
    public ResponseEntity<?> countPayments(@RequestParam Map<String, String> params) {
        try {
            long count = paymentsServices.countPayments(params);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", count
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi server khi đếm payments", "message", ex.getMessage()));
        }
    }
    
    /**
     * Kiểm tra trạng thái payment - GET /api/payments/{id}/status
     */
    @GetMapping("/payments/{id}/status")
    public ResponseEntity<?> checkPaymentStatus(@PathVariable("id") int id) {
        try {
            PaymentsDTO payment = paymentsServices.getPaymentById(id);
            if (payment == null || payment.getId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy payment"));
            }
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "paymentId", payment.getId(),
                    "status", payment.getStatus(),
                    "method", payment.getMethod(),
                    "amount", payment.getAmount(),
                    "transactionCode", payment.getTransactionCode(),
                    "paidAt", payment.getPaidAt()
            ));
            
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi server khi kiểm tra trạng thái", "message", ex.getMessage()));
        }
    }
    
    
    @GetMapping("/payments/vnpay/return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        try {
            System.out.println("VNPay Return Data: " + params);

            String responseCode = params.get("vnp_ResponseCode"); // "00" = success
            //String enrollmentIdStr = params.get("enrollmentId");
            String orderInfo = params.get("vnp_OrderInfo");
            String enrollmentIdStr = null;
            if (orderInfo != null && orderInfo.startsWith("Thanh toan enrollmentId=")) {
                enrollmentIdStr = orderInfo.replace("Thanh toan enrollmentId=", "").trim();
            }

            if (enrollmentIdStr == null) {
                return ResponseEntity.badRequest().body("Missing enrollmentId");
            }

            // Lấy enrollment
            EnrollmentDTO enrollment = enrollmentServices.getEnrollmentById(Integer.parseInt(enrollmentIdStr));
            if (enrollment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enrollment not found");
            }

            String txnRef = params.get("vnp_TxnRef");
            if (txnRef == null || txnRef.isBlank()) {
                return ResponseEntity.badRequest().body("Missing vnp_TxnRef");
            }

            // Lấy payment theo transactionCode
            List<PaymentsDTO> payments = paymentsServices.getPayments(Map.of("transactionCode", txnRef));
            if (payments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
            }
            PaymentsDTO payment = payments.get(0);

            // Cập nhật trạng thái
            if ("00".equals(responseCode)) {
                payment.setStatus("success");
                enrollment.setStatus("success");
            } else {
                payment.setStatus("failed");
                enrollment.setStatus("failed");
            }

            paymentsServices.addOrUpdate(payment);
            enrollmentServices.addOrUpdate(enrollment);

            // Redirect về React course content
            String courseId = enrollment.getCourseId() != null ? enrollment.getCourseId().toString() : "";
            String redirectUrl = "http://localhost:3000/courses/content/" + courseId;

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xử lý VNPay Return");
        }
    }

}
    
