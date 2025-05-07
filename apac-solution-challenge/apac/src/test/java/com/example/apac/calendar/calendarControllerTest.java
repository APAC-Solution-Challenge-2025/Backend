package com.example.apac.calendar;

import com.example.apac.calendar.controller.CalendarController;
import com.example.apac.calendar.dto.CalendarDay;
import com.example.apac.calendar.dto.CalendarResponse;
import com.example.apac.calendar.service.CalendarService;
import com.example.apac.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(CalendarController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class calendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CalendarController calendarController;

    @MockitoBean
    private CalendarService calendarService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = standaloneSetup(calendarController)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("정상적인 JWT로 캘린더를 조회하면 캘린더 응답을 반환한다")
    void getCalendar_returnExpectedResponse() throws Exception {
        //given
        String email = "test@google.com";
        int year = 2025;
        int month = 5;


        //when
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .signWith(key)
                .compact();

        CalendarResponse responseForTest = new CalendarResponse(
                year, month,
                List.of(
                        new CalendarDay("2025-05-01", 3),
                        new CalendarDay("2025-05-02", 2)
                )
        );

        when(calendarService.getCalendar(email, year, month)).thenReturn(responseForTest);


        //when&then
        mockMvc.perform(get("/api/calendar")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .param("year", "2025")
                        .param("month", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2025))
                .andExpect(jsonPath("$.month").value(5))
                .andExpect(jsonPath("$.days[0].goalAchievedCount").value(3))
                .andExpect(jsonPath("$.days[1].goalAchievedCount").value(2))
                .andDo(document("calendar/get-calendar-success"));;
    }

    @Test
    @DisplayName("잘못된 JWT로 요청 시 400 상태 코드를 반환한다")
    void getCalendar_withInvalidJwt_returnsBadRequest() throws Exception {
        //given
        String invalidToken = "Bearer invalid.jwt.token";

        //when&then
        mockMvc.perform(get("/api/calendar")
                        .header(HttpHeaders.AUTHORIZATION, invalidToken)
                        .param("year", "2025")
                        .param("month", "5"))
                .andExpect(status().isBadRequest())
                .andDo(document("calendar/get-calendar-fail"));;
    }

}
