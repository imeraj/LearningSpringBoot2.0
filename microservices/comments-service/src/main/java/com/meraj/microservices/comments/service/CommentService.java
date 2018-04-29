package com.meraj.microservices.comments.service;

import com.meraj.microservices.comments.model.Comment;
import com.meraj.microservices.comments.repository.CommentRepository;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentService {
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

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = "imageservice"),
            key = "comment.new"
    ))
    public void createComment(Comment newComment) {
        commentRepository
                .save(newComment)
                .log("commentService-save")
                .subscribe();
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
