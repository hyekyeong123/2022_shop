package com.coding.shop.Service;

import com.coding.shop.dto.MemberFormDto;
import com.coding.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public Member creteAdminMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("대전광역시 선화동 행복아파트");
        memberFormDto.setPassword("1234");

        // 메서드를 호출
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){

        Member member = creteAdminMember(); // 멤버 객체 생성
        Member saveMember = memberService.saveMember(member);

        // 생성한 멤버 객체와 저장된 멤버 객체가 같은지 비교
        assertEquals(member.getEmail(), saveMember.getEmail());
    }

    @Test
    @DisplayName("중복 회원 테스트")
    public void saveDuplicateMemberTest(){
        Member member = creteAdminMember();
        Member member2 = creteAdminMember();
        Member saveMember = memberService.saveMember(member);

        // 예외 처리
        Throwable e = assertThrows(IllegalStateException.class, ()->{
            Member saveMember2 = memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }

}