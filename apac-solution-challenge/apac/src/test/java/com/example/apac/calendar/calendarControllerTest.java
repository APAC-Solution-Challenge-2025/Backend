package com.example.apac.calendar;

import com.example.apac.calendar.controller.CalendarController;
import com.example.apac.calendar.dto.CalendarDayDTO;
import com.example.apac.calendar.dto.CalendarResponseDTO;
import com.example.apac.calendar.service.CalendarService;
import com.example.apac.security.JwtUtil;
import com.google.api.Authentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarController.class)
public class calendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalendarService calendarService;

    @MockitoBean
    private Authentication authentication;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("GET /api/calendar - 정상 요청 시 CalendarResponseDTO 반환")
    void getCalendar_returnExpectedResponse() throws Exception {
        // given
        String email = "test@example.com";
        int year = 2025;
        int month = 5;
        List<CalendarDayDTO> days = Arrays.asList(
                new CalendarDayDTO("2025-05-01", 2),
                new CalendarDayDTO("2025-05-02", 3)
        );
        CalendarResponseDTO responseDTO = new CalendarResponseDTO(year, month, days);

        when(calendarService.getCalendar(eq(email), eq(year), eq(month))).thenReturn(responseDTO);

        // when & then
        mockMvc.perform(get("/api/calendar")
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(year))
                .andExpect(jsonPath("$.month").value(month))
                .andExpect(jsonPath("$.days[0].date").value("2025-05-01"))
                .andExpect(jsonPath("$.days[0].goalAchievedCount").value(2))
                .andExpect(jsonPath("$.days[1].date").value("2025-05-02"))
                .andExpect(jsonPath("$.days[1].goalAchievedCount").value(3));
    }
}
