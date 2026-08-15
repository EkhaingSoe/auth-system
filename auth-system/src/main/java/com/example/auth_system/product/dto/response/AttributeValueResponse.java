package com.example.auth_system.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueResponse {
    private UUID id;
    private String value;
    private String hexCode;
    private Integer displayOrder;
    private Boolean isActive;
}
