package com.meraj.imageservice.controller;

import com.meraj.imageservice.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("/images")
    Flux<Image> getImages() {
        return Flux.just(
                new Image("1", "learning-spring-boot-cover.jpg"),
                new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"),
                new Image("3", "bafinzga.png")
        );
    }

    @PostMapping("/images")
    Mono<Void> createImage(@RequestBody Flux<Image> images) {
        return images
                .map(image -> {
                    log.info("We will save" + image + " to a Reactive database soon!");
                    return image;
                }).then();
    }

}
