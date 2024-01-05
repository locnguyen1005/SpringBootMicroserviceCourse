package com.example.accountservice.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Calendar;
import java.util.Date;

import com.example.accountservice.Entity.AccountEntity;
import com.example.accountservice.Repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;


import com.example.accountservice.DTO.AccountDTO;

import com.example.accountservice.Event.AccountProducer;


import com.google.gson.Gson;

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
	@Autowired
	private ResourceLoader resourceLoader;
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
	public Mono<AccountDTO> createAccount(AccountDTO accountDTO ) throws IOException{
		if((checkDuplicate(accountDTO).block()).equals(Boolean.TRUE)) {
			return Mono.error(new com.example.commonservice.Common.CommonException(accountDTO.getEmail(), "Account duplicate", HttpStatus.BAD_REQUEST));
		}
		else {

			accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
			accountDTO.setRole("USER");
			accountDTO.setAvaterimage("1703874564499_Avatar.jpg");
			accountDTO.setBalance(0l);


			return Mono.just(accountDTO)
					.map(newAccountDTO -> mapper.map(newAccountDTO, AccountEntity.class))
					.flatMap(account -> accountRepository.save(account))
					.map(accountEntity -> mapper.map(accountEntity, AccountDTO.class));
		}
	}
	public Mono<AccountDTO> createAccountTeacher(AccountDTO accountDTO , MultipartFile file){
		if((checkDuplicate(accountDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.error(new com.example.commonservice.Common.CommonException(accountDTO.getEmail(), "Account duplicate", HttpStatus.BAD_REQUEST));
		}
		else {
			accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
			accountDTO.setRole("TEACHER");
			File fileObj = convertMultiPartFileToFile(file);
	        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
	        fileObj.delete();
	        accountDTO.setAvaterimage(fileName);
	        accountDTO.setBalance(0l);			
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
	public Mono<AccountEntity> editAccount(Long accountid, AccountDTO account , MultipartFile file) {
	 	AccountEntity accountEntity= accountRepository.findById(accountid).block();
	 	if(file.isEmpty()) {
	 		accountEntity.setAddress(account.getAddress());
	 		accountEntity.setFullname(account.getFullname());
	 		accountEntity.setPassword(passwordEncoder.encode(account.getPassword()));
	 		return accountRepository.save(accountEntity);
	 	}
	 	else {
	 		accountEntity.setAddress(account.getAddress());
	 		accountEntity.setFullname(account.getFullname());
	 		accountEntity.setPassword(passwordEncoder.encode(account.getPassword()));
	 		File fileObj = convertMultiPartFileToFile(file);
	        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
	        fileObj.delete();
	        accountEntity.setAvaterimage(fileName);
	        return accountRepository.save(accountEntity);
	 	}
	}
	public Mono<AccountEntity> accountTeachTransferMoney(Long accountid , Long balance){
		AccountEntity accountEntity =  accountRepository.findById(accountid).block();
		accountEntity.setBalance(accountEntity.getBalance()+balance);
		return accountRepository.save(accountEntity);
	}
}
