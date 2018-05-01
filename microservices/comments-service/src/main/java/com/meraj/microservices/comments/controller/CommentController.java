package com.meraj.microservices.comments.controller;

import com.meraj.microservices.comments.model.Comment;
import com.meraj.microservices.comments.repository.CommentRepository;
import com.meraj.microservices.comments.service.CommentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

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
}
