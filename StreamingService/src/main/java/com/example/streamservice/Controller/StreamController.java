package com.example.streamservice.Controller;

import java.io.IOException;

import com.example.streamservice.Model.CommentStream;
import com.example.streamservice.Model.Stream;
import com.example.streamservice.StreamService.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("Stream")
@CrossOrigin(origins = "http://localhost:3006/")
public class StreamController {
	@Autowired
	private StreamService streamService;


    @GetMapping("/get-key")
    public String getKey() {
        return streamService.getCurrentKey();
    }
	
	@MessageMapping("/sendNotification")
    @SendTo("/topic/notifications")
    public CommentStream sendNotification(CommentStream notification) {
		
        return notification;
    }
	
	
	@GetMapping("/record")
    public String recordVideo() {
        try {
            String inputUrl = "http://localhost:8080/hls/1234.m3u8";
            String outputFilePath = "D:\\SpringBootMicroservice-master\\ffmpeg-6.1-essentials_build\\bin\\record\\1234.mp4";
            streamService.recordHLS(inputUrl, outputFilePath);

            return "Video recorded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error recording video: " + e.getMessage();
        }
    }
	@PostMapping("/createStream")
    public Mono<Stream> createStream(@RequestBody Stream stream) {
        try {
            String inputUrl = "http://localhost:8080/hls/"+stream.getSecretkey()+".m3u8";
            String outputFilePath = "D:\\SpringBootMicroservice-master\\ffmpeg-6.1-essentials_build\\bin\\record\\"+stream.getSecretkey()+".mp4";
            
            
            return streamService.createStream(stream , inputUrl , outputFilePath);
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
	
	@GetMapping("/GetLessionStream/{lessionid}")
	public Mono<Stream> getLessionStream(@PathVariable Long lessionid){
		
		return null;
	}
}
