package me.yejin.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yejin.springbootdeveloper.domain.Comment;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCommentResponse {
    private Long id;
    private String comment;

    public AddCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getContent();
    }
}
