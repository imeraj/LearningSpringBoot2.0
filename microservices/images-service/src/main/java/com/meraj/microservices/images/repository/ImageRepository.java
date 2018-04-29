package com.meraj.microservices.images.repository;

import com.meraj.microservices.images.model.Image;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ImageRepository extends ReactiveCrudRepository<Image, Integer>,
        ReactiveQueryByExampleExecutor {

}
