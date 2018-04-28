package com.meraj.imageservice.images.controller;

import com.meraj.imageservice.comments.repository.CommentRepository;
import com.meraj.imageservice.comments.service.CommentService;
import com.meraj.imageservice.images.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class ImageController {
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;
    private final CommentService commentService;

    public ImageController(ImageService imageService, CommentService commentService) {
        this.commentService = commentService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.findAllImages()
                .flatMap(image -> Flux.just(image)
                                      .zipWith(commentService.findAllComments(image.getId())
                                      .collectList()))
                .map(imageWithComments -> new HashMap<String, Object>() {{
                    put("id", imageWithComments.getT1().getId());
                    put("name", imageWithComments.getT1().getName());
                    put("comments", imageWithComments.getT2());
                }}));
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
