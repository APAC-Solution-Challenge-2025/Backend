package com.example.apac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalendarDayDTO {
        private String date;
        private int goalAchievedCount;
    }
}

