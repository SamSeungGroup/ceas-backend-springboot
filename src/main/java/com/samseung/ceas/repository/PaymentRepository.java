package com.samseung.ceas.repository;

import com.samseung.ceas.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByProduct_Id(Long productId);
    List<Payment> findByBuyer_Id(String userId);
    Payment findByImpUid(String impUid);
}