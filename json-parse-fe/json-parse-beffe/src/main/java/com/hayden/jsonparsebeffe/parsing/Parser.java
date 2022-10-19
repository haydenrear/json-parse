package com.hayden.jsonparsebeffe.parsing;

import com.hayden.dynamicparse.decompile.Decompile;
import com.hayden.dynamicparse.parse.DynamicParseJson;
import com.hayden.dynamicparse.parse.DynamicParsingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class Parser {

  private final Decompile decompile;
  private final DynamicParseJson dynamicParseJson;


  public Optional<String> parse(String json) throws DynamicParsingException, IOException {

    String directory = "src/main/resources/dynamic/";
    String fileName = UUID.randomUUID().toString();

    var toReturn = dynamicParseJson.dynamicParse(json, fileName, Optional.empty(), Optional.of(directory))
      .map(clzz -> this.decompile.decompile(clzz.clzz().getName()));

    File file = new File(directory+fileName+".class");
    file.delete();

    return toReturn;

  }

}
