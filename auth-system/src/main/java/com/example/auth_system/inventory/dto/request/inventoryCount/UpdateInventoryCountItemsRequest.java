package com.example.auth_system.inventory.dto.request.inventoryCount;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryCountItemsRequest {

    @NotEmpty(message = "Count items are required")
    @Valid
    private List<CountedItemRequest> items;

    private String notes;
}
