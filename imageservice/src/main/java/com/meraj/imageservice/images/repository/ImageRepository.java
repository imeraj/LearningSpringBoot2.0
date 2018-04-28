package com.meraj.imageservice.images.repository;

import com.meraj.imageservice.images.model.Image;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ImageRepository extends ReactiveCrudRepository<Image, Integer>,
        ReactiveQueryByExampleExecutor {

}
