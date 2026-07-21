package com.example.auth_system.inventory.dto.request.inventoryCount;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyInventoryCountRequest {

    private String notes;
}
