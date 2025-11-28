package com.surest.member.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.surest.member.dto.MemberDto;
import com.surest.member.jwt.JwtTokenProvider;
import com.surest.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void addMember_ShouldReturnCreatedMember() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setFirstName("John");
        memberDto.setLastName("Doe");

        Mockito.when(memberService.createMember(Mockito.any(MemberDto.class))).thenReturn(memberDto);

        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getMemberById_ShouldReturnMember() throws Exception {
        UUID uuid = UUID.randomUUID();
        MemberDto memberDto = new MemberDto();
        memberDto.setFirstName("Jane");
        memberDto.setLastName("Smith");

        Mockito.when(memberService.getMemberById(uuid)).thenReturn(memberDto);

        mockMvc.perform(get("/api/v1/member/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void getAllMembers_ShouldReturnList() throws Exception {
        MemberDto member1 = new MemberDto();
        member1.setFirstName("Alice");
        member1.setLastName("Brown");

        MemberDto member2 = new MemberDto();
        member2.setFirstName("Bob");
        member2.setLastName("White");

        Mockito.when(memberService.getAllMemberss()).thenReturn(List.of(member1, member2));

        mockMvc.perform(get("/api/v1/member"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].firstName").value("Bob"));
    }

    @Test
    void deleteMemberById_ShouldReturnSuccessMessage() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/member/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().string("Member Deleted Successfully"));

        Mockito.verify(memberService, Mockito.times(1)).deleteMemberById(uuid);
    }

    @Test
    void updateMember_ShouldReturnUpdatedMember() throws Exception {
        UUID uuid = UUID.randomUUID();
        MemberDto updatedMember = new MemberDto();
        updatedMember.setFirstName("Updated");
        updatedMember.setLastName("User");

        Mockito.when(memberService.updateMember(Mockito.eq(uuid), Mockito.any(MemberDto.class)))
                .thenReturn(updatedMember);

        mockMvc.perform(put("/api/v1/member/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }
}
