package me.yejin.springbootdeveloper.config.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import me.yejin.springbootdeveloper.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

// AuthorizationRequestRepository 인터페이스
// 클라이언트 애플리케이션의 OAuth2 인증 요청을 쿠키에 저장하고 해당 요청을 검색, 삭제 기능 제공
// 인증 요청을 저장한 쿠키는 클라이언트 브라우저에 저장, stateless 유지 (인증 과정에서 리다이렉션 등 많이 일어나기 때문에 인증 요청 객체를 저장할 필요가 있음)
@Log4j2
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    private final static int COOKIE_EXPIRE_SECONDS = 18000; // 60 * 60 * 5 즉, 5hour


    // 사용자가 인증 요청을 시작할 때 호출, 해당 인증 요청을 쿠키에 저장
    // OAuth2AuthorizationRequest 객체를 저장
    //TODO : 쿠키를 암호화해서 저장할 수 있지 않을까
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {

        log.info("사용자 oauth2 인증 요청 시작");

        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    // 특정 요청에서 OAuth2 인증 요청 검색
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {

        log.info("사용자 oauth2 인증 요청 검색");

        // request에서 oauth2_auth_request 이름의 쿠키를 찾고 -> 역직렬화 -> OAuth2AuthorizationRequest 객체로 변환
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class); // 쿠키 -> 객체 , 해당 쿠키가 없다면 null 반환
    }

    // 인증 프로세스 완료 or 실패 후 해당 인증 요청 삭제하기 위해 호출됨 ?
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }


    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);

    }
}
