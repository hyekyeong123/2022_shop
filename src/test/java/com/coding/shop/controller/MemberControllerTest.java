package com.coding.shop.controller;

import com.coding.shop.Service.MemberService;
import com.coding.shop.dto.MemberFormDto;
import com.coding.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.plugins.MockMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

//  *******************************************************************

    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() throws Exception{

        // 로그인전 회원 생성
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email,password);

        mockMvc.perform(
            formLogin().userParameter("email")
            .loginProcessingUrl("/member/login")
            .user(email).password(password)
        )
        .andExpect(SecurityMockMvcResultMatchers.authenticated());

    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginFail() throws Exception{

        // 로그인전 회원 생성
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email,password);

        mockMvc.perform(
            formLogin().userParameter("email")
            .loginProcessingUrl("/member/login")
            .user(email).password("12345")
        )
        .andExpect(SecurityMockMvcResultMatchers.unauthenticated());

    }

// http.formLogin() - http 를 통해 들어오는 form 기반 request 를 이용하여 Login 을 처리
/*
- formLogin() - form 태그 기반의 로그인 인증 방식(http.formLogin())을 테스트하기 위해 form 태그 기반 POST request 객체를 생성
- loginProcessingUrl() - 요청 URL 설정 (formLogin() 메소드의 매개변수로 설정해도됨)
- userParameter("email") - formLogin 방식에서 Request 메시지 Body 부분은 "username"=value 가 default 값이기 때문에 "email"=value 로 변경
- user().password() - Requset 메시지 Body Data 설정
*/
    @Test
    void loginError() {
    }
}