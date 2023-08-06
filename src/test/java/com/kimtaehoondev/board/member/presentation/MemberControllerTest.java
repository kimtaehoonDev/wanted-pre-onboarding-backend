package com.kimtaehoondev.board.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimtaehoondev.board.exception.impl.MemberNotFoundException;
import com.kimtaehoondev.board.member.application.MemberService;
import com.kimtaehoondev.board.member.application.dto.MemberInfo;
import com.kimtaehoondev.board.member.presentation.dto.MemberResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @Test
    @WithMockUser
    @DisplayName("사용자를 조회한다")
    void searchMember() throws Exception {
        Long memberId = 1L;
        MemberInfo memberInfo = makeMemberInfo(memberId, "email");
        when(memberService.getMember(memberId)).thenReturn(memberInfo);

        MvcResult mvcResult = mockMvc.perform(get("/api/members/" + memberId))
            .andExpect(status().isOk())
            .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        MemberResponse response = objectMapper.readValue(contentAsString, MemberResponse.class);

        assertThat(response.getEmail()).isEqualTo(memberInfo.getEmail());
        assertThat(response.getId()).isEqualTo(memberInfo.getId());
    }

    @Test
    @WithMockUser
    @DisplayName("존재하지 않는 사용자를 조회하면, 404 상태를 반환한다")
    void memberNotFound() throws Exception {
        Long memberId = 1L;
        doThrow(MemberNotFoundException.class)
            .when(memberService).getMember(memberId);

        MvcResult mvcResult = mockMvc.perform(get("/api/members/" + memberId))
            .andExpect(status().isNotFound())
            .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEmpty();
    }

    public MemberInfo makeMemberInfo(Long memberId, String email) {
        return new MemberInfo() {
            @Override
            public Long getId() {
                return memberId;
            }

            @Override
            public String getEmail() {
                return email;
            }
        };
    }
}