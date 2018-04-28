package com.meraj.imageservice.images.initializers;

import com.meraj.imageservice.images.model.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {
    @Bean
    CommandLineRunner init(MongoOperations mongoOperations) {
        return args -> {
//            mongoOperations.dropCollection(Image.class);

//            mongoOperations.insert(new Image("1", "learning-spring-boot-cover.jpg"));
//            mongoOperations.insert(new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"));
//            mongoOperations.insert(new Image("3", "bafinzga.png"));
//
//            mongoOperations.findAll(Image.class).forEach(image -> {
//                System.out.println(image.toString());
//            });
        };
    }
}
