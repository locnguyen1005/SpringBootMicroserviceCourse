package com.example.streamservice.Event;

import com.example.commonservice.utils.ConstantCommon;
import com.example.streamservice.Model.Stream;
import com.example.streamservice.StreamService.StreamService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
public class StreamComsummer {
    @Autowired
    Gson gson;
    @Autowired
    private StreamService streamService;
    @KafkaListener(id = "LessionService", topics = ConstantCommon.LESSION_STREAM)
    public void createStream(String req){
        Stream stream = gson.fromJson(req , Stream.class);
        try {
            String inputUrl = "http://localhost:8080/hls/"+stream.getSecretkey()+".m3u8";
            String outputFilePath = "D:\\DACN\\ffmpeg-6.1-essentials_build\\bin\\record\\"+stream.getSecretkey()+".mp4";

            Mono<Stream> resp = streamService.createStream(stream , inputUrl , outputFilePath);
        } catch (IOException e) {
            log.info(e.toString());
        }
    }
}
