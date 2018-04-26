package com.meraj.imageservice.repository;

import com.meraj.imageservice.model.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, Integer> {
    Mono<Image> findByName(String imageName);
}
