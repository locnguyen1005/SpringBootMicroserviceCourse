package com.example.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.Common.CommonException;
import com.example.demo.Config.Config;
import com.example.demo.DTO.AccountDTO;

import com.example.demo.Entity.AccountEntity;
import com.example.demo.Event.AccountProducer;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.utils.ConstantCommon;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountService {

	@Autowired
	private AccountProducer accountProducer;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired 
	private ModelMapper mapper;
	@Autowired
	Gson gson = new Gson();
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
    private JwtService jwtService;

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucketName;
/*return lessionRepository.findAll().map(lessionEntity -> mapper.map(lessionEntity, LessionClient.class))
	.flatMap(lessionClient -> getvideoapi(lessionClient))
	.switchIfEmpty(Mono.error(new Exception("Lession Empty")));*/
	public Flux<AccountDTO> getAllAccount() {
		return accountRepository.findAll()
				.map(accountEntity -> mapper.map(accountEntity , AccountDTO.class))
				.flatMap(accountclient -> getvideoapi(accountclient))
				.switchIfEmpty(Mono.error(new Exception("Account Empty")));
	}
	public Mono<Boolean> checkDuplicate(AccountDTO accountDTO){
		return accountRepository.findByEmail(accountDTO.getEmail())
				.flatMap(accountEntity -> Mono.just(true))
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<AccountDTO> createAccount(AccountDTO accountDTO , MultipartFile file){
		if((checkDuplicate(accountDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.error(new CommonException(accountDTO.getEmail(), "Account duplicate", HttpStatus.BAD_REQUEST));
		}
		else {
			accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
			accountDTO.setRole("USER");
			File fileObj = convertMultiPartFileToFile(file);
	        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
	        fileObj.delete();
	        accountDTO.setAvaterimage(fileName);
	        
			return Mono.just(accountDTO)
					.map(newAccountDTO -> mapper.map(newAccountDTO, AccountEntity.class))
					.flatMap(account -> accountRepository.save(account))
					.map(accountEntity -> mapper.map(accountEntity, AccountDTO.class));
		}
	}
	public Mono<AccountDTO> createAccountTeacher(AccountDTO accountDTO , MultipartFile file){
		if((checkDuplicate(accountDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.error(new CommonException(accountDTO.getEmail(), "Account duplicate", HttpStatus.BAD_REQUEST));
		}
		else {
			accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
			accountDTO.setRole("TEACHER");
			File fileObj = convertMultiPartFileToFile(file);
	        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
	        fileObj.delete();
	        accountDTO.setAvaterimage(fileName);

			return Mono.just(accountDTO)
					.map(newAccountDTO -> mapper.map(newAccountDTO, AccountEntity.class))
					.flatMap(account -> accountRepository.save(account))
					.map(accountEntity -> mapper.map(accountEntity, AccountDTO.class))
					.doOnError(throwable -> log.error(throwable.getMessage()))
	                .doOnSuccess(dto -> {
	                	accountProducer.send("AccountSendTeacher", gson.toJson(accountDTO));
	                });
		}
	}
	public Mono<AccountDTO> deleteAccount(AccountDTO accountDTO){
		if((checkDuplicate(accountDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.just(accountDTO)
					 .map(account -> mapper.map(account, AccountEntity.class))
					 .map(accountEntity -> accountRepository.deleteById(accountEntity.getId()))
					 .map((accountEntity -> mapper.map(accountEntity, AccountDTO.class)));
		}
		else {

			return Mono.error(new Exception());
		}
	}


    public Mono<AccountDTO> findAccount(String accountDTO){
		return accountRepository.findByEmail(accountDTO).map(accountEntity -> mapper.map(accountEntity , AccountDTO.class))
				.flatMap(accountclient -> getvideoapi(accountclient))
				.switchIfEmpty(Mono.error(new Exception("Account Empty")));
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
	public Mono<AccountDTO> getvideoapi(AccountDTO lessionClient) {
		lessionClient.setAvaterimage(generatePreSignedUrl(lessionClient.getAvaterimage(),HttpMethod.GET));
		return Mono.just(lessionClient);
	}
	public Mono<AccountDTO> getAccountbyID(Long accountid){
		return accountRepository.findById(accountid).map(accountEntity -> mapper.map(accountEntity , AccountDTO.class))
				.flatMap(accountclient -> getvideoapi(accountclient))
				.switchIfEmpty(Mono.error(new Exception("Account Empty")));
	}
}
