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
            .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL
            .logoutSuccessUrl("/"); // 로그아웃 성공시 이동할 URL

        http.authorizeRequests() // 시큐리티 처리에 HttpServletRequest를 이용함

            // 모든 사용자가 인증 없이 경로 접근 가능
            .mvcMatchers(
                    "/"
                    ,"/members/**"
                    ,"/item/**"
                    ,"/images/**"
            ).permitAll()

            .mvcMatchers("/admin/**").hasRole("ADMIN")

            .anyRequest().authenticated(); // 그 밖에는 모두 인증 요구

        // 인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
