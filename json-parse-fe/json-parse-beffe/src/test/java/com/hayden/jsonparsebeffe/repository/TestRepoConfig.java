package com.hayden.jsonparsebeffe.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@Testcontainers
public class TestRepoConfig {

  @Container
  public static GenericContainer<?> containerCreated = new GenericContainer<>(DockerImageName.parse("mongo:latest"));

  @Test
  public void test(){
    System.out.println("hello");
  }

  @BeforeAll
  public static void before(){
    containerCreated.setExposedPorts(List.of(9000));
    containerCreated.addEnv("MONGO_INITDB_ROOT_USERNAME", "admin");
    containerCreated.addEnv("MONGO_INITDB_ROOT_PASSWORD", "admin");
  }

}
