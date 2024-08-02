package me.yejin.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.domain.User;
import me.yejin.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

// UserDetailsService 인터페이스 : 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
//username(email)으로 우선 사용자 정보를 먼저 가져옴
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {
         return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));

    }
}
