package com.samseung.ceas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String impUid;

    @Column(nullable = false, unique = true)
    private String merchantUid;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long paidAmount;

    @Column(nullable = false)
    private LocalDateTime paidDate;

    @Column(nullable = false)
    private String payMethod;

    @Column(nullable = false)
    private String pgProvider;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean success;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User buyer;

    private Long canceledAmount;

    private LocalDateTime canceledDate;
}