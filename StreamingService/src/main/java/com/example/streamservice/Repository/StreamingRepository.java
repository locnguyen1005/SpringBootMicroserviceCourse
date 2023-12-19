package com.example.streamservice.Repository;

import com.example.streamservice.Model.Stream;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface StreamingRepository extends ReactiveMongoRepository<Stream, String>{

}
