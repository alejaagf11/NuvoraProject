package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetSummaryMemoryDTO {
    private String summary;
    private String lastBudgetRecommendation;
    private String updatedAt;
}
