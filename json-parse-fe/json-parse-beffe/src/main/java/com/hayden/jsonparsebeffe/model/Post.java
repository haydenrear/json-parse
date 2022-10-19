package com.hayden.jsonparsebeffe.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document("posts")
@Data
@Component
@Scope("prototype")
public class Post {
  @Id
  String id;
  String text;
}
