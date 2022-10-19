package com.hayden.jsonparsebeffe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hayden.dynamicparse.decompile.Decompile;
import com.hayden.dynamicparse.decompile.DecompilePrinter;
import com.hayden.dynamicparse.decompile.LoadClass;
import com.hayden.dynamicparse.parse.DynamicParseJson;
import com.hayden.jsonparsebeffe.model.RequestClass;
import com.hayden.jsonparsebeffe.parsing.Parser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WebFluxTest
public class TestParseController {

  @Autowired
  Parser parser;
  @Autowired
  WebTestClient webTestClient;
  @Autowired
  ParseController parseController;

  String testRequest = "{\"hello\": \"goodbye\"}";

  @SneakyThrows
  @Test
  public void testSecurity(){
    var r = new RequestClass();
    r.setRequest(testRequest);
    webTestClient.mutateWith(mockOAuth2Login())
      .mutateWith(SecurityMockServerConfigurers.csrf())
      .get()
      .uri("/")
      .exchange()
      .expectStatus()
      .is2xxSuccessful();
    webTestClient.post()
      .uri("/parseJsonRequest")
      .contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(testRequest), String.class)
      .exchange()
      .expectStatus()
      .is4xxClientError();
    webTestClient.mutateWith(mockOAuth2Login())
      .mutateWith(SecurityMockServerConfigurers.csrf())
      .post()
      .uri("/parseJsonRequest")
      .contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(testRequest), String.class)
      .exchange()
      .expectStatus()
      .is2xxSuccessful();
  }



  @SneakyThrows
  @Test
  public void testController(){
    StepVerifier.create(parseController.parsedString(testRequest))
      .expectSubscription()
      .consumeNextWith(System.out::println)
      .verifyComplete();
  }

  @SneakyThrows
  @Test
  public void deleteFiles(){
    StepVerifier.create(parseController.parsedString(testRequest))
      .expectSubscription()
      .consumeNextWith(System.out::println)
      .verifyComplete();
    assertThat(Objects.requireNonNull(new File("src/main/resources/dynamic/").listFiles()).length).isEqualTo(0);
  }

  @Test
  public void testParserDoesNotThrow() {
    assertDoesNotThrow(() -> parser.parse(testRequest));
  }

  @SneakyThrows
  @Test
  public void testParser(){
    var returned = parser.parse(testRequest);
    assertThat(returned).isPresent();
    returned.map(returnedFound -> {
      assertThat(returnedFound).contains("hello");
      assertThat(returnedFound).contains("String");
      System.out.println(returnedFound);
      return returnedFound;
    });
  }

  @SneakyThrows
  @Test
  public void testParserTwo(){
    var returned = parser.parse(testRequest);
    var returnedTwo = parser.parse("{\"ok\":[1,2,3]}");
    assertThat(returned).isPresent();
    returned.map(returnedFound -> {
      assertThat(returnedFound).contains("hello");
      assertThat(returnedFound).contains("String");
//      System.out.println(returnedFound);
      return returnedFound;
    });
    System.out.println(returnedTwo.get());
  }

  @Configuration
  static class Config {

    @Bean
    ParseController parseController(Parser p){
      return new ParseController(p);
    }

    @Bean
    DynamicParseJson dynamicParseJson(){
      return new DynamicParseJson(om());
    }

    @Bean
    DecompilePrinter decompilePrinter(){
      return new DecompilePrinter();
    }

    @Bean
    Parser parser(DynamicParseJson d, Decompile decompile){
      return new Parser(d, decompile);
    }

    @Bean
    LoadClass loadClass(){
      return new LoadClass();
    }

    @Bean
    ObjectMapper om(){
      return new ObjectMapper();
    }

  }


}
