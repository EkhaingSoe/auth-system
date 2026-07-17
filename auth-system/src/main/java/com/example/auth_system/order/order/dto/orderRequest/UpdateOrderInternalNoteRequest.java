package com.example.auth_system.order.order.dto.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderInternalNoteRequest {

    private String internalNotes;

}
