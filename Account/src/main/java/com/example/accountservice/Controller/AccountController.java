package com.example.accountservice.Controller;

import java.io.IOException;
import java.io.InputStream;

import com.example.accountservice.Event.AccountProducer;
import com.example.commonservice.Model.Account;
import com.example.commonservice.Model.Mail;
import com.example.commonservice.utils.ConstantCommon;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import com.example.accountservice.DTO.AccountDTO;

import com.example.accountservice.Entity.AccountEntity;

import com.example.accountservice.Repository.AccountRepository;
import com.example.accountservice.Service.AccountService;
import com.example.accountservice.Service.JwtService;

import com.example.accountservice.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:3006/") // Replace with the origin of your React app
@RequestMapping("Account")
@Slf4j
public class AccountController {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	Gson gson = new Gson();
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AccountProducer accountProducer;
	@Autowired
	private JwtService jwtService;
	@GetMapping("/GetAll")
	public Flux<AccountDTO> getAllAccount() {
		return accountService.getAllAccount();
	}

	@PostMapping("/Create")
	public ResponseEntity<Mono<AccountDTO>> createAccount(@RequestParam(value = "data" , required = false) String requestStr ) throws JsonSyntaxException, IOException {
		InputStream inputStream = AccountController.class.getClassLoader()
				.getResourceAsStream(Constant.JSON_CREATE_ACCOUNT);
		com.example.commonservice.utils.CommonValidate.jsonValidate(requestStr, inputStream);
		log.info(requestStr);
		log.info(requestStr);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(accountService.createAccount(gson.fromJson(requestStr, AccountDTO.class)));
	}
	@PostMapping("/CreateTeacher")
	public ResponseEntity<Mono<AccountDTO>> createAccountTeacher(@RequestParam("data") String requestStr) throws JsonSyntaxException, IOException {
		InputStream inputStream = AccountController.class.getClassLoader()
				.getResourceAsStream(Constant.JSON_CREATE_ACCOUNT);
		com.example.commonservice.utils.CommonValidate.jsonValidate(requestStr, inputStream);
		log.info(requestStr);
		log.info(requestStr);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(accountService.createAccount(gson.fromJson(requestStr, AccountDTO.class)));
	}
	@PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AccountDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
        	log.info(jwtService.generateToken(authRequest.getEmail()));
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
	@GetMapping("/login")
	public Mono<AccountDTO> login(){	
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Mono<AccountDTO> accountEntity = accountService.findAccount(userDetails.getUsername());
			return accountEntity;
	}
	@GetMapping("/getEmail")
	public Mono<AccountDTO> getaccountId(@RequestBody String email){	
		return accountService.findAccount(email);
	}
	@GetMapping("/{accountid}")
	public Mono<AccountDTO> getaccountById(@PathVariable Long accountid){
		return accountService.getAccountbyID(accountid);
	}
	@PutMapping("/Edit/{accountid}")
	public String editAccount(@PathVariable Long accountid ,@RequestParam("data" ) String requestStr , @RequestParam(value = "file" , required = false) MultipartFile file){
		InputStream inputStream = AccountController.class.getClassLoader()
				.getResourceAsStream(Constant.JSON_CREATE_ACCOUNT);
		com.example.commonservice.utils.CommonValidate.jsonValidate(requestStr, inputStream);
		Mono<AccountEntity> accountDTOMono = accountService.editAccount(accountid, gson.fromJson(requestStr, AccountDTO.class),file);
		log.info(accountDTOMono.block().toString());
		log.info(requestStr);
		return jwtService.generateToken(gson.toJson(accountDTOMono.block()));
	}
	@PostMapping("/RegisterUser")
	public String register(@RequestParam("data") String requestStr ){
		String jwt = jwtService.generateToken(requestStr);
		String verification = "http://localhost:9006/Account/createAccountjwt/"+jwt;
		AccountDTO accountDTO = gson.fromJson(requestStr,AccountDTO.class);
		Mail mail = new Mail();
		mail.setRecipient(accountDTO.getEmail());
		mail.setMsgBody("<html><body style=\"text-align: center\"> <h1>Xác thực email</h1>"

				+"<p>Xin chào "+accountDTO.getFullname()+":<p>"
				+"<p>Chúc mừng bạn đã đăng ký thành công tài khoản  "+accountDTO.getEmail()+":<p>"
				+"<p>Vui lòng hoàn tất thao tác cuối cùng để chúng tôi xác thực và bảo vệ tài khoản của bạn tốt hơn.<p>"
				+"<a href="+verification+" style=\"background-color: #3498db; color: #fff; padding: 10px 20px; text-decoration: none; display: inline-block; border-radius: 5px;\">Xác Thực Ngay</a>");
		mail.setSubject("Xác thực email");
		String status = accountProducer.send(ConstantCommon.EMAIL,gson.toJson(mail)).block();
		log.info(mail.toString());
		return "true";
	}
	@GetMapping("/JWTTOJSON/{JWT}")
	public String JWTTOJSON(@PathVariable String JWT ){
		return jwtService.decodeJwt(JWT);
	}
	@GetMapping("/createAccountjwt/{JWT}")
	public String JWTTOJSON(@PathVariable String JWT , HttpServletResponse response) throws IOException {
		AccountDTO accountDTO = gson.fromJson(jwtService.decodeJwt(JWT) , AccountDTO.class);
		Mono<AccountDTO> account= accountService.createAccount(accountDTO);
		log.info(account.block().toString());
		response.sendRedirect("http://localhost:3006/login");
		return jwtService.decodeJwt(JWT);
	}
	
}
