package com.coding.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

// 커스텀을 원할때
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor(){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userId = "";

        // 현재 로그인 한 사용자의 정보를 가져와서 등록
        if(authentication != null){
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
}
