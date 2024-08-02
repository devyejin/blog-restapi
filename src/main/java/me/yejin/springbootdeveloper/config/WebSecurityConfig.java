package me.yejin.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 시큐리티(인증, 인가) 사용 비활성화 설정
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/user")
                        ).permitAll()
                        .anyRequest().authenticated()) //인증이 성공해야 접근 가능
                .formLogin(formLogin -> formLogin // 시큐리티 폼 기반 로그인 설정
                        .loginPage("/login")
                        .defaultSuccessUrl("/articles")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true) // 세션 삭제 처리 (기존 플젝에서는 모르고 직접 삭제했는데, 시큐리티에서 기능 제공함)
                )
                .csrf(AbstractHttpConfigurer::disable) // local이니 csrf 비활성화
                .build();
    }

    //인증 관리자(AuthenticationMager) 설정
    @Bean
    public AuthenticationManager authenticationMager(HttpSecurity http,
                                                     BCryptPasswordEncoder bCryptPasswordEncoder,
                                                     UserDetailService userDetailService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService); // 사용자 정보를 가져올 서비스 설정, UserDetailsService 인터페이스 구현체
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return new ProviderManager(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
