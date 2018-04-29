package com.meraj.imageservice.comments.controller;

import com.meraj.imageservice.comments.model.Comment;
import com.meraj.imageservice.comments.service.CommentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {
    private final RabbitTemplate rabbitTemplate;

    public CommentController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment
                .flatMap(comment ->
                    Mono.fromRunnable(() ->
                        rabbitTemplate.convertAndSend("imageservice", "comment.new", comment))
                 )
                .then(Mono.just("redirect:/"));
    }
}
