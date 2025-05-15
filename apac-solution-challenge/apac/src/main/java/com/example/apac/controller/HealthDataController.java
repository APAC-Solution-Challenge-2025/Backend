package com.example.apac.controller.data;

import com.example.apac.dto.data.HealthStatusDto;
import com.example.apac.service.HealthStatusService;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.firebase.auth.FirebaseAuth;

@RestController
@RequestMapping("/api/data")
@Tag(name = "HealthStatusController", description = "회원가입 시 사용자 정보 관련 API - 보호자 / 출산직후 여성")
public class HealthStatusController {

    private final HealthStatusService healthStatusService;

    @Autowired
    public HealthStatusController(HealthStatusService healthStatusService) {
        this.healthStatusService = healthStatusService;
    }

    @Operation(summary = "사용자 정보(보호자 / 출산직후 여성) DB 저장")
    @PostMapping("/health-status")
    public ResponseEntity<String> saveHealthData(@RequestBody HealthStatusDto request,
                                                 @RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String userId = firebaseToken.getUid();

            healthStatusService.saveHealthDataFromDto(userId, request);

            return ResponseEntity.ok("health status 데이터 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("!! health status 데이터 저장 실패: " + e.getMessage());
        }
    }
}
