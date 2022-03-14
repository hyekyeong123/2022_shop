package com.coding.shop.Service;

import com.coding.shop.entity.Member;
import com.coding.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor // 아래 메서드(생성자) 호출
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /*public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    // 멤버 저장하기
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // 멤버 유효성 검사(중복된 이메일이 있으면 X)
    private void validateDuplicateMember(Member member) {

        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }

    // 이메일로 멤버 정보 가져오기(스프링 시큐리티)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member findMember = memberRepository.findByEmail(email);

        // 알맞은 멤버를 찾지 못하였다면
        if(findMember == null){
            throw new IllegalStateException("알맞은 회원 정보를 찾을 수 없습니다."+email);
        }

        return User.builder()
                .username(findMember.getName())
                .password(findMember.getPassword())
                .roles(findMember.getRole().toString())
                .build();
    }
}
