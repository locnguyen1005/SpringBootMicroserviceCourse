package com.example.demo.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.example.demo.Common.CommonException;
import com.example.demo.DTO.LessionClient;
import com.example.demo.DTO.LessionDTO;

import com.example.demo.Entity.LessionEntity;
import com.example.demo.Model.Product;
import com.example.demo.Repository.LessionRepository;



import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
@Service
@Slf4j
public class LesssionService {

	@Autowired
	private WebClient.Builder webBuilder;

	private static final String Class = null;
	@Autowired
	private LessionRepository lessionRepository;
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	public Flux<LessionClient> getAllAccount() {
		return lessionRepository.findAll().map(lessionEntity -> mapper.map(lessionEntity, LessionClient.class))
				.flatMap(lessionClient -> getvideoapi(lessionClient))
				.switchIfEmpty(Mono.error(new Exception("Lession Empty")));
	}

	public Mono<Boolean> checkDuplicate(LessionDTO lessionDTO) {
		return lessionRepository.findByTitle(lessionDTO.getTitle()).flatMap(lessionEntiy -> Mono.just(true))
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}

	//Tạo Lession
	public Mono<LessionDTO> createLession(LessionDTO lessionDTO , MultipartFile file) throws IOException {

		try {
//			if ((checkDuplicate(lessionDTO).block()).equals(Boolean.TRUE)) {
//				return Mono.error(
//						new CommonException(lessionDTO.getTitle(), "Name lession duplicate", HttpStatus.BAD_REQUEST));
//			}
			File fileObj = convertMultiPartFileToFile(file);
	        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
	        fileObj.delete();
	        lessionDTO.setVideo(fileName);
	        lessionDTO.setDate(LocalDateTime.now().toString());
			return Mono.just(lessionDTO).map(newLession -> mapper.map(newLession, LessionEntity.class))
					.flatMap(newLessionEntity -> lessionRepository.save(newLessionEntity))
					.map(accountEntity -> mapper.map(accountEntity, LessionDTO.class));
		} catch (Exception e) {
			return Mono.error(e);

		}

	}
	//xóa lession
	public Mono<LessionDTO> deleteAccount(LessionDTO lessionDTO) {
		if ((checkDuplicate(lessionDTO).block()).equals(Boolean.TRUE)) {
			return Mono.just(lessionDTO).map(lession -> mapper.map(lession, LessionEntity.class))
					.map(lessionEntity -> lessionRepository.deleteById(lessionEntity.getId()))
					.map((accountEntity -> mapper.map(accountEntity, LessionDTO.class)));
		} else {
			return Mono.error(new Exception());
		}
	}
	//Lấy tất cả lession trong product
	public Flux<LessionDTO> getAllLessionByProductId(Long productId) {
		return lessionRepository.findByProductId(productId)
				.map(lessionEntity -> mapper.map(lessionEntity, LessionDTO.class))
				.switchIfEmpty(Mono.error(new Exception("Lession Empty")));
	}
	//lấy lession theo id
	public Mono<LessionDTO> getLesssionbyID(LessionDTO lessionDTO) {
		return lessionRepository.findById(lessionDTO.getId())
				.map(lessionEntity -> mapper.map(lessionEntity, LessionDTO.class))
				.switchIfEmpty(Mono.error(new Exception("Lession Empty")));
	}

	public Mono<LessionDTO> updatelession(LessionDTO lessionDTO) throws IOException {
		return null;
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
	public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded : " + fileName;
    }
	
	
	public Mono<LessionClient> getvideoapi(LessionClient lessionClient) {
		lessionClient.setVideoapi(generatePreSignedUrl(lessionClient.getVideo(),HttpMethod.GET));
		return Mono.just(lessionClient);
	}
}
