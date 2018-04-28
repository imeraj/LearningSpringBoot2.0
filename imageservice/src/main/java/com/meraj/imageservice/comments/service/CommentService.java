package com.meraj.imageservice.comments.service;

import com.meraj.imageservice.comments.model.Comment;
import com.meraj.imageservice.comments.repository.CommentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Flux<Comment> findAllComments(String imageId) {
        return commentRepository.findByImageId(imageId);
    }
}
