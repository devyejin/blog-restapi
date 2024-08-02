package me.yejin.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yejin.springbootdeveloper.domain.Article;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createAt;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createAt = article.getCreatedAt();
    }
}
