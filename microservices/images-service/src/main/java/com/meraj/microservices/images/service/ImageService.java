package com.meraj.microservices.images.service;

import com.meraj.microservices.images.model.Image;
import com.meraj.microservices.images.repository.ImageRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Example;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;

    public ImageService(ResourceLoader resourceLoader, ImageRepository imageRepository) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
    }

    public Flux<Image> findAllImages() {
        return imageRepository.findAll();
    }

    public Mono<Resource> findOneImage(String filename) {
        return Mono.fromSupplier(() ->
                resourceLoader.getResource(
                        "file:" + UPLOAD_ROOT + "/" + filename));
    }

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .flatMap(file -> {
                    Mono<Image> saveDatabaseImage = imageRepository.save(
                            new Image(
                                    UUID.randomUUID().toString(),
                                    file.filename()));

                    Mono<Void> copyFile = Mono.just(
                            Paths.get(UPLOAD_ROOT, file.filename())
                                    .toFile())
                            .log("createImage-picktarget")
                            .map(destFile -> {
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-newfile")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");

                    return Mono.when(saveDatabaseImage, copyFile);
                })
                .then();
    }

    public Mono<Void> deleteImage(String filename) {
        Image image = new Image();
        image.setName(filename);
        Example<Image> example = Example.of(image);
        Flux<Image> deleteImages = imageRepository.findAll(example);

        Flux<Void> deleteDatabaseImage = deleteImages.
                flatMap(deleteImage ->
                     imageRepository.delete(deleteImage)
                );

        Mono<Void> deleteFile = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return Mono.when(deleteDatabaseImage, deleteFile)
                .then();
    }
}
