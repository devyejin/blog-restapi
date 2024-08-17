package me.yejin.springbootdeveloper.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.yejin.springbootdeveloper.config.jwt.TokenProvider;
import me.yejin.springbootdeveloper.domain.RefreshToken;
import me.yejin.springbootdeveloper.domain.User;
import me.yejin.springbootdeveloper.repository.RefreshTokenRepository;
import me.yejin.springbootdeveloper.service.UserService;
import me.yejin.springbootdeveloper.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

//reuse 프로젝트할 때는 SimpleUrlAuthenticationSuccessHandler 이용해서 config에서만 설정해줬었음
//이번에는 쿠키에 토큰 저장 작업을 추가해서 상속받아 오버라이드해서 작업
//클라이언트 앱이 인증 코드를 사용해서 인증 서버로 부터 액세스 토큰을 받고서 (인증 최종 완료)  OAuth2SuccessHandler 동작
//사용자(User) 정보를 클라이언트 앱에 저장하는 작업은 앞 선 OAuth2UserCustomService 에서 처리
@RequiredArgsConstructor
@Component
@Log4j2
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/articles";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    //인증 성공시
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("인증 서버로부터 사용자 인증 완료, 사용자에게 액세스, 리프레쉬 토큰 작업 시작");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); // 인증 서버로부터 받은 OAuth2User 객체는 Authentication 에 보관
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        //리프레시 토큰 생성 -> db 저장 -> 쿠키에 저장 (Refresh Token은 서버에 저장하고 Access 는 클라가 저장)
        //TODO : TEST RDB -> Redis 로 변경
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken); // db저장
        addRefreshTokenToCookie(request, response, refreshToken); //쿠키에 리프레시 토큰 저장

        //액세스 토큰 생성 -> 쿼리파라미터에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        // 인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response);

        //리다이렉트 ( 액세스 토큰은 쿼리 파라미터, 리프레쉬 토큰은 쿠키로 전달)
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }


    //생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    //생성된 리프레시토큰 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    //액세스 토큰을 path에 추가, 쿼리 파라미터로 전달
    //TODO : 보안에 취약하므로 HTTP 헤더 방식으로 개선
    private String getTargetUrl(String token) {
        String url = UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();

        log.info("target Url = {}", url);
        return url;
    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request,response);
    }

}
