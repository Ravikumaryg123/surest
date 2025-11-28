package com.surest.member.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surest.member.dto.MemberDto;
import com.surest.member.exception.BusinessServiceException;
import com.surest.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    private MemberDto testMember;

    @BeforeEach
    void setUp() throws BusinessServiceException {
        // Create a test member in DB before each test
        testMember = new MemberDto();
        testMember.setFirstName("John");
        testMember.setLastName("Doe");
        testMember.setDateOfBirth(LocalDate.parse("2000-12-10"));
        String uniqueEmail = "user_" + UUID.randomUUID() + "@example.com";
        testMember.setEmail(uniqueEmail);
        testMember = memberService.createMember(testMember);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddMember() throws Exception {
        MemberDto newMember = new MemberDto();
        newMember.setFirstName("Jane");
        newMember.setLastName("Smith");
        newMember.setEmail("user_" + UUID.randomUUID() + "@example.com");
        newMember.setDateOfBirth(LocalDate.parse("2000-12-10"));

        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMember)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetMemberById() throws Exception {
        testMember.setEmail("abc@example.com");
        mockMvc.perform(get("/api/v1/member/{uuid}", testMember.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetAllMembers() throws Exception {
        testMember.setEmail("joof@example.com");
        mockMvc.perform(get("/api/v1/member"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateMember() throws Exception {
        testMember.setLastName("Updated");
        mockMvc.perform(put("/api/v1/member/{uuid}", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/member/{uuid}", testMember.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Member Deleted Successfully"));
    }
}
