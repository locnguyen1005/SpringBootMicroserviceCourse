package com.example.streamservice.Event;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.commonservice.utils.ConstantCommon;
import com.example.streamservice.Model.Stream;
import com.example.streamservice.StreamService.StreamService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class StreamComsummer {
    @Autowired
    Gson gson;
    @Autowired
    private StreamService streamService;
    @Autowired
    private AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @KafkaListener(id = "LessionService", topics = ConstantCommon.LESSION_STREAM)
    public void createStream(String req){
        Stream stream = gson.fromJson(req , Stream.class);
        log.info(req);
        try {
            String inputUrl = "http://localhost:8080/hls/"+stream.getSecretkey()+".m3u8";
            String outputFilePath = "D:\\DACN\\ffmpeg-6.1-essentials_build\\bin\\record\\"+stream.getSecretkey()+".mp4";

            String ffmpegPath = "D:\\DACN\\ffmpeg-6.1-essentials_build\\bin\\ffmpeg.exe";
            String command = "D:\\DACN\\ffmpeg-6.1-essentials_build\\bin\\ffmpeg.exe -i http://localhost:8080/hls/"
                    + stream.getSecretkey() + ".m3u8"
                    + " -c copy -f hls -hls_time 10 -hls_list_size 6 -hls_flags delete_segments D:\\SpringBootMicroservice-master\\ffmpeg-6.1-essentials_build\\bin\\record\\"
                    + stream.getSecretkey() + ".mp4";
            FFmpeg ffmpeg = new FFmpeg("D:\\DACN\\ffmpeg-6.1-essentials_build\\bin\\ffmpeg.exe");
            FFmpegBuilder job = ffmpeg.builder()
                    .setInput("http://localhost:8080/hls/" + stream.getSecretkey() + ".m3u8")
                    .addOutput("D:\\DACN\\ffmpeg-6.1-essentials_build\\bin\\record\\"+stream.getSecretkey()+".mp4")
                    .done();
            new FFmpegExecutor(ffmpeg).createJob(job).run();

            File livestreamFile = new File("D:\\DACN\\ffmpeg-6.1-essentials_build\\bin\\record\\"+ stream.getSecretkey() + ".mp4");
            String fileName = System.currentTimeMillis() + "_" + livestreamFile.getName();
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, livestreamFile));
            stream.setSecretkey(fileName);
            stream.setDate(LocalDateTime.now().toString());
        } catch (IOException e) {
            log.info(e.toString());
        }
    }
}
