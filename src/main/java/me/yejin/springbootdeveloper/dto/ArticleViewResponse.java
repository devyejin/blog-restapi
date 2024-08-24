package me.yejin.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yejin.springbootdeveloper.domain.Article;
import me.yejin.springbootdeveloper.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {

    private Long id;
    private String author;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private List<Comment> comments;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.author = article.getAuthor();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createAt = article.getCreatedAt();
        this.comments = article.getComments();
    }
}
