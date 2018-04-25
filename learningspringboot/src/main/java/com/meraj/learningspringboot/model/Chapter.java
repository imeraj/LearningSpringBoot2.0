package com.meraj.learningspringboot.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Chapter {
    @Id
    private String id;
    private String name;

    public Chapter(String name) {
        this.name = name;
    }
}
