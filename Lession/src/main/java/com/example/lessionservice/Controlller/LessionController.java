package com.example.lessionservice.Controlller;

import java.io.InputStream;

import com.example.lessionservice.DTO.LessionClient;
import com.example.lessionservice.DTO.LessionDTO;
import com.example.lessionservice.Entity.LessionEntity;
import com.example.lessionservice.Service.LesssionService;
import com.example.lessionservice.Utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.commonservice.Model.Answer;
import com.example.commonservice.utils.CommonValidate;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequestMapping("/Lession")
@CrossOrigin(origins = "http://localhost:3006")
@RestController
@Slf4j
public class LessionController {
	@Autowired
	private LesssionService lesssionService;
	@Autowired
	Gson gson = new Gson();
	
	@GetMapping("/getall")
	public Flux<LessionClient> getall() {
		return lesssionService.getAllAccount().filter(lession -> lession.getDeletesoft()== 0l);
	}
	@PostMapping("/Create")
	public ResponseEntity<Mono<LessionDTO>> createAccount(@RequestParam("data") String requestStr , @RequestParam(value = "file") MultipartFile file)throws Exception{
			//đổi chuỗi String qua json
			InputStream inputStream = LessionController.class.getClassLoader().getResourceAsStream(Constant.JSON_CREATE_ACCOUNT);
			CommonValidate.jsonValidate(requestStr, inputStream);
			LessionDTO lessionDTO = gson.fromJson(requestStr,LessionDTO.class);
			return ResponseEntity.status(HttpStatus.CREATED).body(lesssionService.createLession(lessionDTO , file));
	}
	@PutMapping("/edit/{id}")
	public ResponseEntity<Mono<LessionEntity>> editlession(@PathVariable Long id, @RequestParam("data") String requestStr , @RequestParam(value = "file") MultipartFile file)throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(lesssionService.updatelession(id,gson.fromJson(requestStr,LessionDTO.class), file));
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
	//Them lession khi product da duoc dky
	@PostMapping("/addLession")
	public ResponseEntity<Mono<LessionDTO>> addLessonToProduct(@RequestParam("data") String requestStr , @RequestParam(value = "file") MultipartFile file)throws Exception{
			//đổi chuỗi String qua json
			InputStream inputStream = LessionController.class.getClassLoader().getResourceAsStream(Constant.JSON_CREATE_ACCOUNT);
			CommonValidate.jsonValidate(requestStr, inputStream);
			LessionDTO lessionDTO = gson.fromJson(requestStr,LessionDTO.class);
			return ResponseEntity.status(HttpStatus.CREATED).body(lesssionService.createLession(lessionDTO , file));
	}
	@PutMapping("/deleteLession/{lessionid}")
	public Mono<LessionEntity> deletelession(@PathVariable Long lessionid){
		return lesssionService.deleteLession(lessionid);
	}
}
