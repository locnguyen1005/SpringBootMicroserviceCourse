package com.example.lessionservice.Service;

import java.io.IOException;

import java.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;


import com.example.lessionservice.DTO.LessionClient;
import com.example.lessionservice.DTO.LessionDTO;
import com.example.lessionservice.Event.LessionProducer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;

import com.example.lessionservice.Entity.LessionEntity;

import com.example.lessionservice.Repository.LessionRepository;
import com.example.commonservice.utils.ConstantCommon;
import com.google.gson.Gson;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;

@Service
@Slf4j
public class LesssionService {
	@Autowired
	Gson gson;
	@Autowired
	private WebClient.Builder webBuilder;
	@Autowired
	private LessionProducer lessionProducer;
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
			lessionDTO.setDeletesoft(0l);
			return Mono.just(lessionDTO).map(newLession -> mapper.map(newLession, LessionEntity.class))
					.flatMap(newLessionEntity -> lessionRepository.save(newLessionEntity))
					.map(accountEntity -> mapper.map(accountEntity, LessionDTO.class))
					.doOnError(throwable -> log.error(throwable.getMessage()))
	                .doOnSuccess(dto -> {
	                	lessionProducer.send(ConstantCommon.LESSION_ACCOUNT,gson.toJson(dto)).subscribe();
	                });
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

	public Mono<LessionEntity> updatelession(Long id ,LessionDTO lessionDTO , MultipartFile file) throws IOException {
		LessionEntity lessionEntity= lessionRepository.findById(id).block();
		if(file.isEmpty()){
			lessionEntity.setTitle(lessionDTO.getTitle());
			lessionEntity.setDescription(lessionDTO.getDescription());
			lessionEntity.setDate(LocalDateTime.now().toString());
			return lessionRepository.save(lessionEntity);
		}
		else {
			File fileObj = convertMultiPartFileToFile(file);
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
			fileObj.delete();
			lessionEntity.setVideo(fileName);
			lessionEntity.setTitle(lessionDTO.getTitle());
			lessionEntity.setDescription(lessionDTO.getDescription());
			lessionEntity.setDate(LocalDateTime.now().toString());
			return lessionRepository.save(lessionEntity);
		}
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
	public Mono<LessionEntity> deleteLession(Long lesssionid){
		LessionEntity lessionEntity =lessionRepository.findById(lesssionid).block();
		lessionEntity.setDeletesoft(1l);
		return lessionRepository.save(lessionEntity);
	}
}
