package com.example.LessionStreamService.LessionStreamController;

import com.example.LessionStreamService.LessionStreamEntity.LessionStream;
import com.example.LessionStreamService.LessionStreamService.LessionStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/LessionStream")
public class LessionStreamController {
    @Autowired
    LessionStreamService lessionStreamService;
    @PostMapping("/createLessionStream")
    public Mono<LessionStream> createLessionStream(@RequestBody LessionStream lessionStream){
        return lessionStreamService.createLessionStream(lessionStream);
    }
    @GetMapping("/getid/{lessionid}")
    public Mono<LessionStream> getbyid(@PathVariable String lessionid){
        return lessionStreamService.getLessionbyid(lessionid);
    }
}
