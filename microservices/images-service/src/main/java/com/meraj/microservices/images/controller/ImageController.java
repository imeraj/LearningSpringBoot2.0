package com.meraj.microservices.images.controller;

import com.meraj.microservices.images.service.ImageService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
public class ImageController {
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;
    private final RestTemplate restTemplate;

    public ImageController(ImageService imageService, RestTemplate restTemplate) {
        this.imageService = imageService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images",
                imageService
                        .findAllImages()
                        .map(image -> new HashMap<String, Object>() {{
                            put("id", image.getId());
                            put("name", image.getName());
                            put("comments",
                                    // tag::comments[]
                                    restTemplate.exchange(
                                            "http://COMMENTS-SERVICE/comments/{imageId}",
                                            HttpMethod.GET,
                                            null,
                                            new ParameterizedTypeReference<List<Comment>>() {},
                                            image.getId()).getBody());
                            // end::comments[]
                        }})
        );
        return Mono.just("index");
    }

    @GetMapping(value = "/images/" + FILENAME + "/raw",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename) {
        return imageService.findOneImage(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok().contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    } catch (IOException e) {
                        return ResponseEntity.badRequest()
                                .body("Couldn't find " + filename + " => " + e.getMessage());
                    }

                });
    }

    @PostMapping("/images")
    public Mono<String> createFile(@RequestPart(name = "file") Flux<FilePart> files) {
        return imageService.createImage(files)
                .then(Mono.just("redirect:/"));
    }

    @DeleteMapping("/images/" + FILENAME)
    public Mono<String> deleteFile(@PathVariable String filename) {
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"));
    }
}
