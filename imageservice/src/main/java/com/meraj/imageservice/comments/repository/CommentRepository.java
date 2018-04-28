package com.meraj.imageservice.comments.repository;

import com.meraj.imageservice.comments.model.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByImageId(String imageId);
}
