package me.yejin.springbootdeveloper;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성을 DB에 위임
    @Column(name = "id", updatable = false)
    @Id
    private Long id;

    @Column(name = "name", nullable = false) //테이블의 컬럼과 객체의 필드를 매핑
    private String name;

    public void changeName(String name) {
        this.name = name;
    }
}
