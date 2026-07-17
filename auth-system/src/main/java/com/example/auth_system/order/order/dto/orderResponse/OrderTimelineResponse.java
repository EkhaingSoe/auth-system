package com.example.auth_system.order.order.dto.orderResponse;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTimelineResponse {
    private LocalDateTime orderDate;
    private LocalDateTime approvedDate;
    private LocalDateTime processingDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private LocalDateTime cancelledDate;
}
