package me.yejin.springbootdeveloper.dto;

import lombok.Getter;
import me.yejin.springbootdeveloper.domain.Article;

@Getter
public class ArticleListViewResponse { // 뷰 전달용
    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }

}
