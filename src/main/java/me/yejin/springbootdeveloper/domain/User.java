package me.yejin.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails { // UserDetails 상속받아 User 인증 객체로 사용, UserDetails : 스프링 시큐리티에서 사용자 인증 정보 담는 인터페이스

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Id
    private Long id;

    //사용자 정보를 조회해서 users 테이블에 있다면 리소스 서버에서 제공해주는 이름으로 업데이트
    //users 테이블에 없다면 새 사용자를 생성해서 데이터베이스에 저장
    @Column(name="nickname", unique = true)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    private String provider;

    private String provierId;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User update(String nickname) {
        this.nickname = nickname;

        return this;
    }

    //권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return password;
    }



    // 사용자의 고유한 값 반환
    @Override
    public String getUsername() {
        return email;
    }

    //TODO : 계정 만료 여부 확인 로직
    @Override
    public boolean isAccountNonExpired() {
        // 만료 여부 확인 로직
        return true; // true = not expired
    }

    //TODO : 계정 잠금 여부 확인 로직
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //TODO : 패스워드 만료 여부 확인 로직
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //TODO : 계정 사용 가능 여부 확인 로직
    @Override
    public boolean isEnabled() {
        return true;
    }
}
