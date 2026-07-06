package com.example.auth_system.order.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoResponse {

    private UUID id;
    private String name;
    private String email;
}
