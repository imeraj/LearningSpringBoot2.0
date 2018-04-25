package com.meraj.learningspringboot.controller;

import com.meraj.learningspringboot.model.Chapter;
import com.meraj.learningspringboot.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChapterController {
    private final ChapterRepository chapterRepository;

    public ChapterController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @GetMapping("/chapters")
    public Flux<Chapter> getChapters() {
        return chapterRepository.findAll();
    }
}
