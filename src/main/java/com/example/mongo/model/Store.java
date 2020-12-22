package com.example.mongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TextScore;

@Data
@Document
public class Store {

    @Id
    private String id;

    @TextIndexed
    private String name;

    @TextIndexed
    private String description;

    //Override database key value of email with contact
    @Field("contact")
    private String email;

    private Boolean emailVerified = false;

    @TextScore
    private Float score;
}
