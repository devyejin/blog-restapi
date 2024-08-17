package me.yejin.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.domain.User;
import me.yejin.springbootdeveloper.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

//리소스 서버에 사용자 정보를 요청할 때, 인증 코드 전달함
@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    //리소스 서버에서 보내주는 사용자 정보를 불러온다.
    //users 테이블에 사용자 정보가 있다면 이름을 리소스 서버에서 보내주는 이름으로 업데이트
    //없다면 saveOrupdate() 메서드를 실행해 DB users 테이블에 회원 데이터를 추가한다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user); // 유저가 DB에 있다면 업데이트, 없다면 생성
        return user;
    }

    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return userRepository.save(user);
    }
}
