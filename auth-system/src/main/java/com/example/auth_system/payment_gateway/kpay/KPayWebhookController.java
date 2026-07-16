package com.example.auth_system.payment_gateway.kpay;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_system.order.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment/kpay")
@RequiredArgsConstructor
public class KPayWebhookController {

    private final PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(
            @RequestBody KPayWebhookRequest request) {

        paymentService.handleKPayWebhook(request);

        return ResponseEntity.ok(Map.of(
                "success", true));
    }

}