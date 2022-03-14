package com.coding.shop.repository;

import com.coding.shop.Service.MemberService;
import com.coding.shop.constant.ItemSellStatus;
import com.coding.shop.constant.Role;
import com.coding.shop.entity.Item;
import com.coding.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.ThrowableCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static com.mysema.commons.lang.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 저장 테스트")
    public void createMember(){
        Member member = new Member();
        member.setEmail("aaa@naver.com");
        member.setName("aaa@naver.com");
        member.setPassword("aaa@naver.com");
        member.setRole(Role.ADMIN);

        Member newMember = memberRepository.save(member);
    }

    @Test
    void findByEmail() {
        this.createMember();
        Member member = memberRepository.findByEmail("aaa@naver.com");
        System.out.println(member);
    }
}