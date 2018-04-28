package com.meraj.imageservice.comments.controller;

import com.meraj.imageservice.comments.model.Comment;
import com.meraj.imageservice.comments.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return commentService.createComment(newComment)
                .then(Mono.just("redirect:/"));
    }
}
