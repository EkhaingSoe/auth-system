package com.example.auth_system.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeResponse {

    private UUID id;
    private String name;
    private String displayName;
    private String attributeType;
    private Boolean isActive;
    private List<AttributeValueResponse> values;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeValueResponse {
        private UUID id;
        private String value;
        private String hexCode;
        private Integer displayOrder;
        private Boolean isActive;
    }
}
