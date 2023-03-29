package com.samseung.ceas.service;

import com.samseung.ceas.model.Payment;
import com.samseung.ceas.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment create(Payment payment){
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment Id: {} is saved", savedPayment.getId());
        return savedPayment;
    }

    public Payment cancel(Payment payment){
        Payment canceledPayment = paymentRepository.save(payment);
        log.info("Payment Id: {} is cancled", canceledPayment.getId());
        return canceledPayment;
    }

    public Payment retrieveByImpUid(String impUid){
        return paymentRepository.findByImpUid(impUid);
    }

    public List<Payment> retrieveByProductId(Long productId) {
        return paymentRepository.findByProduct_Id(productId);
    }

    public List<Payment> retrieveByBuyerId(String userId) {
        return paymentRepository.findByBuyer_Id(userId);
    }
}