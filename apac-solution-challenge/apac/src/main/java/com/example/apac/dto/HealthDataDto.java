package com.example.apac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthDataDto {

    @JsonProperty("selectedHealth")
    private HealthStatus healthStatus;

    @Getter
    @Setter
    public static class HealthStatus {
        private List<String> userType;
        private List<String> hobbies;
        private List<String> condition;
        private List<String> birthDate;
        private List<String> birthMethod;
    }
}

