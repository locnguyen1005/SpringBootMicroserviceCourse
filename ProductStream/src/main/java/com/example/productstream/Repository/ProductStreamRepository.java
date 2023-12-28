package com.example.productstream.Repository;

import com.example.productstream.Entity.ProductStream;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductStreamRepository  extends ReactiveMongoRepository<ProductStream , String> {
    Mono<ProductStream> findByName(String name);
}
