package me.yejin.springbootdeveloper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.domain.Article;
import me.yejin.springbootdeveloper.domain.Comment;
import me.yejin.springbootdeveloper.dto.*;
import me.yejin.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController  // response 객체 데이터를 JSON으로 반환
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody @Valid AddArticleRequest request, Principal principal) {
        Article savedArticle = blogService.save(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList(); // Article -> ArticleReponse

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) { // @PathVariable : url 에서 값 가져옴
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id,
                                                 @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }

    @PostMapping("/api/comments")
    public ResponseEntity<AddCommentResponse> addComment(@RequestBody AddCommentRequest request, Principal principal){
        Comment savedComment = blogService.addComment(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AddCommentResponse(savedComment));

    }
}
