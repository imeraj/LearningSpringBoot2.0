package com.meraj.microservices.images.controller;

import com.meraj.microservices.images.model.Comment;
import com.meraj.microservices.images.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {
    private final RestTemplate restTemplate;

    public CommentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment.map(comment ->
                restTemplate.postForEntity("http://COMMENTS-SERVICE/comments",
                        comment, Comment.class)
        ).then(Mono.just("redirect:/"));
    }
}
