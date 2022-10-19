package com.hayden.jsonparsebeffe.repo;

import com.hayden.jsonparsebeffe.model.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends ReactiveMongoRepository<Post, String> {
}
