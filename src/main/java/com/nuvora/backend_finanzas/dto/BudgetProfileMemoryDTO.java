package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetProfileMemoryDTO {

    private String preferredStyle;
    private String incomeType;
    private String savingPriority;
    private String debtLevel;
    private List<String> mainExpenseCategories;
    private String lastUpdated;

}
