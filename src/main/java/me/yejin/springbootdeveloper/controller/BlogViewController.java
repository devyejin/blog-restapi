package me.yejin.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.domain.Article;
import me.yejin.springbootdeveloper.dto.ArticleListViewResponse;
import me.yejin.springbootdeveloper.dto.ArticleViewResponse;
import me.yejin.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String articles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    //신규, 수정 화면은 같은 화면을 사용한다. 쿼리 파라미터 존재 유무로 판단
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse()); // 타임리프의 경우, 빈 객체라도 넘겨줘야 에러가 발생하지 않음
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
