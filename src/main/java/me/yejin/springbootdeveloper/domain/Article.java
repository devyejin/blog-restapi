package me.yejin.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Article {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    @Id
    private Long id;

    @Column(name="title", nullable=false)
    private String title;

    @Column(name="content", nullable=false)
    private String content;

    @Builder // 빌더 패턴, 생성자로 객체 생성할 때보다 명시적이라 가독성 높음
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //Entity가 수정되는거니까 dao에서
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
