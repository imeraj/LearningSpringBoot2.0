package com.meraj.learningspringboot.repository;

import com.meraj.learningspringboot.model.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository extends ReactiveCrudRepository<Chapter, String> {

}
