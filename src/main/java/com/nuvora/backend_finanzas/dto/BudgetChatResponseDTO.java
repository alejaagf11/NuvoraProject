package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetChatResponseDTO {
    private String message;// texto para frontend
    private String source;// de donde salio
}
