package me.yejin.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name="content", nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    private Article article;

    @Builder
    public Comment(Article article, String author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
    }
}
