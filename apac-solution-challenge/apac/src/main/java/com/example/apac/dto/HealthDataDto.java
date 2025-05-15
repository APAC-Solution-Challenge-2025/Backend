package com.example.apac.dto;

import java.util.List;
import java.util.Map;

public class HealthDataDto {
    private Map<String, List<String>> healthStatus;

    public Map<String, List<String>> getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(Map<String, List<String>> healthStatus) {
        this.healthStatus = healthStatus;
    }
}
