package com.meraj.imageservice.controller;

import com.meraj.imageservice.model.Image;
import com.meraj.imageservice.service.ImageService;
import com.sun.tools.internal.ws.processor.model.Response;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.findAllImages());
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
