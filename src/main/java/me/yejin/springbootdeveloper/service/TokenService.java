package me.yejin.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.config.jwt.TokenProvider;
import me.yejin.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

// 액세스 토큰 생성 API
// 유효한 리프레시 토큰이라면 새로운 액세스 토큰을 생성한다.
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        //토큰 유효성 검증, 실패하면 예외를 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        //리프레시 토큰으로 사용자ID를 찾아 액세스 토큰을 생성
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));

    }

}
