package com.meraj.microservices.comments.controller;

import com.meraj.microservices.comments.model.Comment;
import com.meraj.microservices.comments.repository.CommentRepository;
import com.meraj.microservices.comments.service.CommentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{imageId}")
    public Flux<Comment> comments(@PathVariable String imageId) {
        return commentService.findAllComments(imageId);
    }

    @PostMapping("/comments")
    public void addComment(@RequestBody Comment newComment) {
        commentService.createComment(newComment);
    }
}
