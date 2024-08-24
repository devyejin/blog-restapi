package me.yejin.springbootdeveloper.repository;

import me.yejin.springbootdeveloper.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
