package com.example.demo.Controlller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.HttpMethod;
import com.example.demo.DTO.LessionClient;
import com.example.demo.DTO.LessionDTO;
import com.example.demo.Model.Answer;
import com.example.demo.Model.Product;
import com.example.demo.Repository.LessionRepository;
import com.example.demo.Service.LesssionService;
import com.example.demo.utils.CommonValidate;
import com.example.demo.utils.ConstantCommon;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;

import ch.qos.logback.classic.Logger;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@RequestMapping("/Lession")
@RestController
@Slf4j
public class LessionController {
	@Autowired
	private LesssionService lesssionService;
	@Autowired
	Gson gson = new Gson();
	
	@GetMapping("/getall")
	public Flux<LessionClient> getall() {
		return lesssionService.getAllAccount();
	}
	@PostMapping("/Create")
	public ResponseEntity<Mono<LessionDTO>> createAccount(@RequestParam("data") String requestStr , @RequestParam(value = "file") MultipartFile file)throws Exception{
			//đổi chuỗi String qua json
			InputStream inputStream = LessionController.class.getClassLoader().getResourceAsStream(com.example.demo.Utils.Constant.JSON_CREATE_ACCOUNT);
			CommonValidate.jsonValidate(requestStr, inputStream);
			LessionDTO lessionDTO = gson.fromJson(requestStr,LessionDTO.class);
			return ResponseEntity.status(HttpStatus.CREATED).body(lesssionService.createLession(lessionDTO , file));
	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<Mono<LessionDTO>> editlession(@RequestBody LessionDTO lessionDTO)throws Exception{
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lesssionService.updatelession(lessionDTO));
	}
	@PostMapping("/Loc")
    public Mono<Answer> demo(@RequestBody Answer answer) {
		log.info("answer true");
		answer.setAccountid(1l);
		log.info(answer.toString());
		return Mono.just(answer);
    }
	@GetMapping("/GetAllBy/{productid}")
	public Flux<LessionDTO> getallbyproductId(@PathVariable Long productid){
		return lesssionService.getAllLessionByProductId(productid);
	}
}
