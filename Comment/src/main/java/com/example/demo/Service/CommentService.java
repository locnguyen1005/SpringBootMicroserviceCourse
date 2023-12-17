package com.example.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Model.Answer;
import com.example.demo.Model.Comment;
import com.example.demo.Model.Quiz;

import com.example.demo.Repository.CommentRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CommentService {
	@Autowired
	private AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucketName;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private WebClient.Builder webBuilder;
	
	@Autowired 
	private ModelMapper mapper;
	
	public Flux<CommentDTO> getAllAnswer(){
		return  commentRepository.findAll()
				.map(accountEntity -> mapper.map(accountEntity , CommentDTO.class))
				.flatMap(accountclient -> getvideoapi(accountclient))
				.switchIfEmpty(Mono.error(new Exception("Account Empty")));
	}
	public Mono<Comment> saveAnswer(Comment comment , MultipartFile file){	
		File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        comment.setImage(fileName);
		return commentRepository.save(comment);
	}
	public Flux<Comment> getAllAnswerbyid(Long productId){
		return  commentRepository.findByProductid(productId);
		
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
	public Mono<CommentDTO> getvideoapi(CommentDTO lessionClient) {
		lessionClient.setImage(generatePreSignedUrl(lessionClient.getImage(),HttpMethod.GET));
		return Mono.just(lessionClient);
	}
//	public Mono<AccountDTO> getvideoapi(AccountDTO lessionClient) {
//		lessionClient.setAvaterimage(generatePreSignedUrl(lessionClient.getAvaterimage(),HttpMethod.GET));
//		return Mono.just(lessionClient);
//	}
}
