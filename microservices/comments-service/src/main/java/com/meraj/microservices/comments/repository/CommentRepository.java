package com.meraj.microservices.comments.repository;

import com.meraj.microservices.comments.model.Comment;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String>,
        ReactiveQueryByExampleExecutor {
    Flux<Comment> saveAll(Flux<Comment> newComment);
}

