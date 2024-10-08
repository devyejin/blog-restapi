package me.yejin.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AutoCloseable.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Article {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    @Id
    private Long id;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name="title", nullable=false)
    private String title;

    @Column(name="content", nullable=false)
    private String content;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    //TODO : Cascade 로 삭제는 안하고 댓글 게시자 마이페이지에서는 조회 가능하도록 처리
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE) // Comment Entity가 article 필드를 사용해서 Article Entity를 참조
    private List<Comment> comments;

    @Builder // 빌더 패턴, 생성자로 객체 생성할 때보다 명시적이라 가독성 높음
    public Article(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    //Entity가 수정되는거니까 dao에서
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
