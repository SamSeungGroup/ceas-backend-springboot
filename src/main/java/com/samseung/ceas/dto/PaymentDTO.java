package com.samseung.ceas.dto;

import com.samseung.ceas.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String impUid;
    private String merchantUid;
    private String productName;
    private Long paidAmount;
    private Long paidMilliseconds;
    private LocalDateTime paidDate;
    private String payMethod;
    private String pgProvider;
    private String status;
    private Boolean success;
    private Long productId;
    private String buyerId;
    private Long canceledAmount;
    private Long canceledMilliseconds;
    private LocalDateTime canceledDate;
    private Long totalPaidNumber;

    public PaymentDTO(Payment payment){
        this.id = payment.getId();
        this.impUid = payment.getImpUid();
        this.merchantUid = payment.getMerchantUid();
        this.productName = payment.getProductName();
        this.paidAmount = payment.getPaidAmount();
        this.paidDate = payment.getPaidDate();
        this.payMethod = payment.getPayMethod();
        this.pgProvider = payment.getPgProvider();
        this.status = payment.getStatus();
        this.success = payment.getSuccess();
        this.productId = payment.getProduct().getId();
        this.buyerId = payment.getBuyer().getId();
        this.canceledAmount = payment.getCanceledAmount();
        this.canceledDate = payment.getCanceledDate();
    }

    public static Payment toEntity(final PaymentDTO dto){
        return Payment.builder()
                .id(dto.getId())
                .impUid(dto.getImpUid())
                .merchantUid(dto.getMerchantUid())
                .productName(dto.getProductName())
                .paidAmount(dto.getPaidAmount())
                .paidDate(Instant.ofEpochMilli(dto.getPaidMilliseconds())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .payMethod(dto.getPayMethod())
                .pgProvider(dto.getPgProvider())
                .status(dto.getStatus())
                .success(dto.getSuccess())
                .build();
    }
}
