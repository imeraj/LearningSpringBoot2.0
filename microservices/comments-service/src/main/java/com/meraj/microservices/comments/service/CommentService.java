package com.meraj.microservices.comments.service;

import com.meraj.microservices.comments.model.Comment;
import com.meraj.microservices.comments.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(Processor.class)
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Flux<Comment> findAllComments(String imageId) {
        Comment comment = new Comment();
        comment.setImageId(imageId);
        Example<Comment> example = Example.of(comment);
        return commentRepository.findAll(example);
    }

    @StreamListener(Processor.INPUT)
    @Output(Processor.OUTPUT)
    public Flux<Comment> createComment(Flux<Comment> newComment) {
        return commentRepository
                .saveAll(newComment)
                .map(comment -> {
                    log.info("Saving new comment: " + comment);
                    return comment;
                });
    }
}
