package com.surest.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surest.member.dto.JwtAuthResponse;
import com.surest.member.dto.LoginDto;
import com.surest.member.jwt.JwtTokenProvider;
import com.surest.member.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ShouldReturnJwtAuthResponse_WhenCredentialsAreValid() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("john");
        loginDto.setPassword("securePassword");

        String expectedToken = "mock-jwt-token";

        Mockito.when(authService.login(Mockito.any(LoginDto.class))).thenReturn(expectedToken);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(expectedToken));
    }
}
