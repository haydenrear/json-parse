package com.hayden.jsonparsebeffe.repository;

import com.app.backendforfrontend.model.Post;
import com.app.backendforfrontend.repo.PostRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(
  properties = {
    "spring.data.mongodb.uri=mongodb://admin:admin@localhost:9000/app?retryWrites=true&w=majority"
  }
)
public class TestRepo extends TestRepoConfig {


  @Autowired
  PostRepo postRepo;
  @Autowired
  ReactiveMongoTemplate mongoClient;

  @Test
  public void test(){
    Post s = new Post();
    postRepo.save(s)
      .subscribe(r -> System.out.println(r.getId()));
  }

  @Test
  void contextLoads() {
  }

}
