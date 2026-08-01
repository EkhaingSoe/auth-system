package com.example.auth_system.inventory.dto.request.inventoryCount;

import com.example.auth_system.inventory.enums.CountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryCountRequest {

    private LocalDateTime scheduledDate;

    private String notes;
}
