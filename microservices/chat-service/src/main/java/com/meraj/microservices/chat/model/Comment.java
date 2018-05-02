package com.meraj.microservices.chat.model;

import lombok.Data;

@Data
public class Comment {
    private String id;
    private String imageId;
    private String comment;
}
