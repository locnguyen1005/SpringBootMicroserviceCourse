package com.example.LessionStreamService.LessionStreamRepository;

import com.example.LessionStreamService.LessionStreamEntity.LessionStream;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface LessionStreamRepository extends ReactiveMongoRepository<LessionStream, String> {
}
