package com.example.LessionStreamService.LessionStreamService;

import com.example.LessionStreamService.Event.LessionStreamProducer;
import com.example.LessionStreamService.LessionStreamDTO.LessionStreamDTO;
import com.example.LessionStreamService.LessionStreamEntity.LessionStream;
import com.example.LessionStreamService.LessionStreamRepository.LessionStreamRepository;
import com.example.commonservice.utils.ConstantCommon;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LessionStreamService {
    @Autowired
    LessionStreamRepository lessionStreamRepository;
    @Autowired
    Gson gson;
    @Autowired
    LessionStreamProducer lessionStreamProducer;
    public Mono<LessionStream> createLessionStream(LessionStream lessionStream){
        String status = lessionStreamProducer.send(ConstantCommon.LESSION_STREAM , gson.toJson(lessionStream)).block();
        return lessionStreamRepository.save(lessionStream);
    }

}
