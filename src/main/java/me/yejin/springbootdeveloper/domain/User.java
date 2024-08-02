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

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
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
