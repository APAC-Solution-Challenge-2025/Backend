package com.example.apac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HealthStatusSummaryDTO {
    private String content;
    private int status;
}
