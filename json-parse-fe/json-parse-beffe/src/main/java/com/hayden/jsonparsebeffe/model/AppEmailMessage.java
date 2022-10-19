package com.hayden.jsonparsebeffe.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;

@Data
@Component(value = "appMessage")
@Scope(value = "prototype")
public class AppEmailMessage {

  String text;
  String from;
  String to;
  File file;

}
