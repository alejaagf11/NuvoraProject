package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageMemoryDTO {
    private String role;
    private String content;
    private String timestamp;
}
