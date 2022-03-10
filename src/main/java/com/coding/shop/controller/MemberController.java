package com.coding.shop.controller;

import com.coding.shop.Service.MemberService;
import com.coding.shop.dto.MemberFormDto;
import com.coding.shop.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 페이지
    @GetMapping(value = "/new")
    public String memberRegisterView(Model model){

        // MemberFormDto의 기본값을 전달
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // 회원가입 액션
    @PostMapping(value = "/new")
    public String memberRegisterAction(
            @Valid MemberFormDto memberFormDto,
            BindingResult bindingResult,
            Model model
    ){

        if(bindingResult.hasErrors()){ // 에러가 있다면 회원 가입 페이지로로
           return "member/memberForm";
        }

        try{
            // 멤버객체 생성하여 저장
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }
}
