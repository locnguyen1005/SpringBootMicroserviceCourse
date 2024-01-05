package com.example.streamservice.StreamService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.example.streamservice.DTO.StreamDTO;
import com.example.streamservice.Model.Stream;
import com.example.streamservice.Repository.StreamingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;

import net.bramp.ffmpeg.FFmpegExecutor;

import net.bramp.ffmpeg.builder.FFmpegBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StreamService {
	@Autowired
	private StreamingRepository streamingRepository;
	@Autowired
	private AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucketName;
	@Autowired
	private ModelMapper mapper;
	
	private String currentKey;
    private LocalDateTime keyExpiration;
    
	
    public String generateAndSetNewKey() {
        // Sinh key ngẫu nhiên
        String newKey = UUID.randomUUID().toString();

        // Set key mới và thời gian hiệu lực
        currentKey = newKey;
        keyExpiration = LocalDateTime.now().plusHours(1);

        return newKey;
    }

    public String getCurrentKey() {
        // Kiểm tra xem key hiện tại có hiệu lực không
        if (keyExpiration == null || keyExpiration.isBefore(LocalDateTime.now())) {
            // Nếu không có hoặc hết hạn, tạo key mới
            generateAndSetNewKey();
        }

        return currentKey;
    }
	public void recordHLS(String inputUrl, String outputFilePath) throws IOException {
		try {

			String ffmpegPath = "D:\\SpringBootMicroservice-master\\ffmpeg-6.1-essentials_build\\bin\\ffmpeg.exe";

			String command = "D:\\SpringBootMicroservice-master\\ffmpeg-6.1-essentials_build\\bin\\ffmpeg.exe -i http://localhost:8080/hls/locnguyen1234.m3u8 -c copy D:\\SpringBootMicroservice-master\\ffmpeg-6.1-essentials_build\\bin\\record\\output1.mp4";

			ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
			
			Process process = processBuilder.start();

			int exitCode = process.waitFor();

			if (exitCode == 0) {
				System.out.println("Lệnh đã thực hiện thành công");
			} else {
				System.out.println("Lệnh thất bại với mã lỗi: " + exitCode);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Mono<Stream> createStream(Stream stream, String inputUrl, String outputFilePath) throws IOException {
		log.info("Service "+ stream.toString());
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
		return streamingRepository.save(stream);
	}
	
	
	//lấy API video
		public String generatePreSignedUrl(String filePath, HttpMethod http) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DAY_OF_YEAR,6);
	        return amazonS3.generatePresignedUrl(bucketName,filePath,cal.getTime(),http).toString();
	    }
		private java.io.File convertMultiPartFileToFile(MultipartFile file) {
	        java.io.File convertedFile = new File(file.getOriginalFilename());
	        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
	            fos.write(file.getBytes());
	        } catch (IOException e) {
	            log.error("Error converting multipartFile to file", e);
	        }
	        return convertedFile;
	    }
		//Upload file
		public String uploadFile(File file) {
	        String fileName = System.currentTimeMillis() + "_" + file.getName();
	        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
	        file.delete();
	        return "File uploaded : " + fileName;
	    }
		
		
		public Mono<StreamDTO> getvideoapi(StreamDTO lessionClient) {
			lessionClient.setVideoapi(generatePreSignedUrl(lessionClient.getVideoapi(),HttpMethod.GET));
			return Mono.just(lessionClient);
		}

		public Flux<StreamDTO> getall() {
			return streamingRepository.findAll().map(lessionEntity -> mapper.map(lessionEntity, StreamDTO.class))
					.flatMap(lessionClient -> getvideoapi(lessionClient))
					.switchIfEmpty(Mono.error(new Exception("Lession Empty")));
		}
	public Mono<StreamDTO> getbyid(String lessionid) {
		return streamingRepository.findById(lessionid).map(lessionEntity -> mapper.map(lessionEntity, StreamDTO.class))
				.flatMap(lessionClient -> getvideoapi(lessionClient))
				.switchIfEmpty(Mono.error(new Exception("Lession Empty")));
	}
}
