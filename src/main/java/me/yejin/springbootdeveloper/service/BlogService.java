package me.yejin.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.config.error.exception.ArticleNotFoundException;
import me.yejin.springbootdeveloper.domain.Article;
import me.yejin.springbootdeveloper.domain.Comment;
import me.yejin.springbootdeveloper.dto.AddArticleRequest;
import me.yejin.springbootdeveloper.dto.AddCommentRequest;
import me.yejin.springbootdeveloper.dto.UpdateArticleRequest;
import me.yejin.springbootdeveloper.repository.BlogRepository;
import me.yejin.springbootdeveloper.repository.CommentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // final or @NotNull 붙은 필드의 생성자 추가
@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public void delete(Long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        //게시글을 작성한 유저인지 확인한다. 불일치시 예외 발생
        authorizeArticleAuthor(article);

        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        //게시글을 작성한 유저인지 확인한다. 불일치시 예외 발생
        authorizeArticleAuthor(article);

        article.update(request.getTitle(), request.getContent()); // 영속성 컨텍스트에서 entity 변경 감지

        return article;
    }

    //게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

    public Comment addComment(AddCommentRequest request, String userName) {
        Article article = blogRepository.findById(request.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getArticleId()));
        return commentRepository.save(request.toEntity(userName, article));
    }

}
