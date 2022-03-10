package com.coding.shop.config;

import com.coding.shop.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            // 로그인
            .formLogin().loginPage("/members/login") // 로그인 페이지 URL 설정
            .defaultSuccessUrl("/") // 로그인 성공시 이동할 URL
            .usernameParameter("email") // 로그인시 사용할 파라미터 이름으로 email
            .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL

            // 로그아웃
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 URL
            .logoutSuccessUrl("/"); // 로그아웃 성공시 이동할 URL
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
