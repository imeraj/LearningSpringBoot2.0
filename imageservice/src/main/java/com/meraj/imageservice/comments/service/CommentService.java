package com.meraj.imageservice.comments.service;

import com.meraj.imageservice.comments.model.Comment;
import com.meraj.imageservice.comments.repository.CommentRepository;
import com.meraj.imageservice.images.model.Image;
import org.springframework.data.domain.Example;
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

    public Mono<Void> createComment(Mono<Comment> newComment) {
        return newComment
                .flatMap(commentRepository::save)
                .then();
    }
}
