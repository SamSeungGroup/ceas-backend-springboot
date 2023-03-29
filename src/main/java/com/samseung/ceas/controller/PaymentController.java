package com.samseung.ceas.controller;

import com.samseung.ceas.dto.PaymentDTO;
import com.samseung.ceas.dto.ResponseDto;
import com.samseung.ceas.dto.ResponseDtos;
import com.samseung.ceas.model.Payment;
import com.samseung.ceas.service.PaymentService;
import com.samseung.ceas.service.ProductService;
import com.samseung.ceas.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    // 포트원 API 토큰 획득
    @GetMapping
    public ResponseEntity<?> getAccessToken() {
        final String REST_API_Key = "1213051550014846";
        final String REST_API_Secret = "OogyP3boSLZN4tQJ4czmYxNY0LB2bKoJaUmwhZF6wBCKLF6iYr07soO4w7AWVREGMDTy4qJZH27Kjxhf";

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/users/getToken";
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("imp_key", REST_API_Key);
        parameter.add("imp_secret", REST_API_Secret);
        HashMap<String, Object> accessToken = restTemplate.postForObject(url, parameter, HashMap.class);

        ResponseDto response = ResponseDto.builder().data(accessToken).build();
        return ResponseEntity.ok().body(response);
    }

    // 상품 결제 정보 조회
    @GetMapping("/products/{product_id}")
    public ResponseEntity<?> retrieveByProductId(@AuthenticationPrincipal String userId, @PathVariable Long product_id) {
        try {
            List<Payment> paymentList = paymentService.retrieveByProductId(product_id);
            List<PaymentDTO> dtos = paymentList.stream().map(PaymentDTO::new).collect(Collectors.toList());
            ResponseDtos<PaymentDTO> response = ResponseDtos.<PaymentDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDto response = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 결제 내역 등록
    @PostMapping("/products/{product_id}")
    public ResponseEntity<?> create(@AuthenticationPrincipal String userId, @PathVariable Long product_id, @RequestBody PaymentDTO dto) {
        Payment payment = PaymentDTO.toEntity(dto);
        payment.setId(null);
        payment.setProduct(productService.retrieve(product_id));
        payment.setBuyer(userService.retrieve(userId));
        Payment savedPayment = paymentService.create(payment);

        PaymentDTO paymentDTO = new PaymentDTO(savedPayment);
        ResponseDto<PaymentDTO> response = ResponseDto.<PaymentDTO>builder().data(paymentDTO).build();
        return ResponseEntity.ok().body(response);
    }

    // 취소 내역 등록
    @PutMapping("/products/{product_id}")
    public ResponseEntity<?> cancel(@AuthenticationPrincipal String userId, @PathVariable Long product_id, @RequestParam PaymentDTO dto) {
        Payment original = paymentService.retrieveByImpUid(dto.getImpUid());
        original.setStatus(dto.getStatus());
        original.setCanceledAmount(dto.getCanceledAmount());
        original.setCanceledDate(Instant.ofEpochMilli(dto.getCanceledMilliseconds())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        Payment cancledPayment = paymentService.cancel(original);

        PaymentDTO paymentDTO = new PaymentDTO(cancledPayment);
        ResponseDto<PaymentDTO> response = ResponseDto.<PaymentDTO>builder().data(paymentDTO).build();
        return ResponseEntity.ok().body(response);
    }

    // 유저 결제 정보 조회
    @GetMapping("/users/{user_id}")
    public ResponseEntity<?> retrieveByUserId(@AuthenticationPrincipal String userId, @PathVariable String user_id) {
        try {
            List<Payment> paymentList = paymentService.retrieveByBuyerId(user_id);
            List<PaymentDTO> dtos = paymentList.stream().map(PaymentDTO::new).collect(Collectors.toList());

            ResponseDtos<PaymentDTO> response = ResponseDtos.<PaymentDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDto response = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}